import React, { useEffect, useCallback, useState } from 'react';
import { Cookies, useCookies } from 'react-cookie';
import { BlogUser, database } from '@/database';
import LoggedUser from '@/database/model/entity/LoggedUser';
import { useConnection } from './ConnectionContext';
import moment from 'moment';
import * as UseSelector from 'use-context-selector';

export type SignInData = {
  user: string;
  password: string;
}

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
      const logged = await database.get<LoggedUser>(LoggedUser.table).query().fetch();
      if (isOffline) {
        if (logged.length === 0) {
          setAuthenticated(false);
          return null;
        }
        const user = await logged[0].user.observe();
        const subscription = user.subscribe((user) => setUser(user!!));
        setAuthenticated(true);
        return subscription;
      }

      if (cookies['app-token']) {
        const user = await logged[0].user.observe();
        const subscription = user.subscribe((user) => setUser(user!!));
        setAuthenticated(true);
        return subscription;
      }

      if (logged.length !== 0) {
        database.write(async () => {
          await logged[0].destroyPermanently()
        });
      }
      setAuthenticated(false);
      return null;
    }

    const subscription = fetch();
    return () => {
      subscription.then((subscription) => subscription?.unsubscribe());
    }
  }, [cookies, isOffline]);


  const signIn = useCallback(async (data: SignInData) => {
    try {
      const request = null || {
        data: {
          login: {
            accessToken: 'debug-token',
            expires: moment().add(2, 'hours').diff(moment())
          }
        }
      };

      const { login: { expires, accessToken } } = request.data;
      var maxAge = expires/1000;

      await database.write(async () => {
        await database.unsafeResetDatabase();
        const user = await database.get<BlogUser>(BlogUser.table).create((user) => {
          user.name = 'Cristian Ferreira';
          user.username = 'cristian_sknz';
          user.email = 'cristian.datingaa@gmail.com';
        });

        await database.get<LoggedUser>(LoggedUser.table).create(logged => {
          logged.user.set(user);
        })
      });

      setCookies('app-token', accessToken, { maxAge });
      setAuthenticated(true);
    } catch (e: any) {
      console.log(e)
      throw 'Endereço de email ou senha estão incorretos.';
    }
  }, []);

  const logout = useCallback(() => {
    removeCookies('app-token');
    setAuthenticated(false);
    database.write(async () => {
      await database.get<BlogUser>(BlogUser.table).query().destroyAllPermanently()
    });
  }, []);

  const flush = useCallback(async () => {
    if (isOffline) {
      const logged = await database.get<LoggedUser>(LoggedUser.table).query().fetch();
      if (logged.length === 0) {
        setAuthenticated(false);
        return;
      }
      setAuthenticated(true);
      return;
    }

    const cookies = new Cookies();
    if (cookies.get('app-token')) {
      if  (!isAuthenticated) setAuthenticated(true);
      return;
    }
    setAuthenticated(false);
  }, [isAuthenticated, isOffline]);

  return <AuthContext.Provider value={{
    isAuthenticated,
    signIn,
    logout,
    user,
    flush
  }}>
    {children}
  </AuthContext.Provider>
}

export { AuthContext };
export const useUser = () => UseSelector.useContextSelector(AuthContext, (value) => value.user);
export const useAuth = () => UseSelector.useContext(AuthContext);
export default AuthProvider;