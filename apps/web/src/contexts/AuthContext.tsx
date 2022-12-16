import React, { useEffect, useCallback, useState } from 'react';
import { Cookies, useCookies } from 'react-cookie';
import { BlogUser, database } from '@/database';
import { useConnection } from './ConnectionContext';
import * as UseSelector from 'use-context-selector';
import { authenticatedFetch, backendFetch } from '@/libs/fetch';
import SyncProvider from './SyncContext';

export type SignInData = {
  username: string;
  password: string;
};

interface AuthContextType {
  isAuthenticated: boolean;
  user: BlogUser | null;
  signIn: (data: SignInData) => void;
  logout: () => void;
  flush: () => void;
}

const AuthContext = UseSelector.createContext<AuthContextType>({} as AuthContextType);

const AuthProvider: React.FC<React.PropsWithChildren> = ({ children }) => {
  const [isAuthenticated, setAuthenticated] = useState<boolean>(false);
  const [user, setUser] = useState<BlogUser | null>(null);
  const [cookies, setCookies, removeCookies] = useCookies();
  const { isOffline } = useConnection();

  useEffect(() => {
    async function fetch() {
      const logged = localStorage.getItem('logged_user')

      if (isOffline) {
        if (!logged) {
          setAuthenticated(false);
          return null;
        }
        const user = await database.get<BlogUser>(BlogUser.table).findAndObserve(logged);
        const subscription = user.subscribe((user) => setUser(user!!));
        setAuthenticated(true);
        return subscription;
      }

      if (cookies['app-token'] && logged) {
        const user = await database.get<BlogUser>(BlogUser.table).findAndObserve(logged);
        const subscription = user.subscribe((user) => setUser(user!!));
        setAuthenticated(true);
        return subscription;
      }

      setAuthenticated(false);
      return null;
    }

    const subscription = fetch();
    return () => {
      subscription.then((subscription) => subscription?.unsubscribe());
    };
  }, [cookies, isOffline]);

  const signIn = useCallback(async (data: SignInData) => {
    const response = await backendFetch('/auth/login', {
      method: 'POST',
      body: new URLSearchParams({
        username: data.username,
        password: data.password,
      }),
    });

    if (response.status != 200) {
      throw 'Endereço de email ou senha estão incorretos.';
    }

    const { expires: maxAge, token } = await response.json();
    setCookies('app-token', token, { maxAge });

    const { id, name, username, email } = await (await authenticatedFetch('/me')).json();
    
    await database.write(async () => {
      await database.unsafeResetDatabase();
      const user = await database.get<BlogUser>(BlogUser.table).create((user) => {
        user._raw.id = id
        user.name = name;
        user.username = username;
        user.email = email;
      });
      
      setUser(user)
    });
    localStorage.setItem('logged_user', id)
    setAuthenticated(true);
  }, []);

  const logout = useCallback(() => {
    removeCookies('app-token');
    setAuthenticated(false);
    database.write(async () => {
      await database.get<BlogUser>(BlogUser.table)
        .query()
        .destroyAllPermanently();
    });
  }, []);

  const flush = useCallback(async () => {
    if (isOffline) {
      const logged = localStorage.getItem('logged_user')
      if (!logged) {
        setAuthenticated(false);
        return;
      }
      setAuthenticated(true);
      return;
    }

    const cookies = new Cookies();
    if (cookies.get('app-token')) {
      if (!isAuthenticated) setAuthenticated(true);
      return;
    }
    setAuthenticated(false);
  }, [isAuthenticated, isOffline]);

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        signIn,
        logout,
        user,
        flush,
      }}
    >
      <SyncProvider isAuthenticated={isAuthenticated}>
        {children}
      </SyncProvider>
    </AuthContext.Provider>
  );
};

export { AuthContext };
export const useUser = () =>
  UseSelector.useContextSelector(AuthContext, (value) => value.user);
export const useAuth = () => UseSelector.useContext(AuthContext);
export default AuthProvider;