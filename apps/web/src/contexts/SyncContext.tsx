import { authenticatedFetch } from '@/libs/fetch';
import React, { useCallback, useEffect, useState } from 'react';
import * as UseSelector from 'use-context-selector';

type SyncContextType = {
  organization: string | null;
  isLoading: boolean
}

type Organization = {
  id: string;
  name: string;
}

const SyncContext = UseSelector.createContext<SyncContextType>({} as SyncContextType)

type SyncProviderProps = {
  isAuthenticated: boolean;
  children: React.ReactNode
}

const SyncProvider: React.FC<SyncProviderProps> = ({ children, isAuthenticated }) => {
  const [organization, setOrganization] = useState<string | null>(localStorage.getItem('organization'));
  const [isLoading, setLoading] = useState<boolean>(true)

  useEffect(() => {
    if (!isAuthenticated) {
      setLoading(false)
      return
    }

    if (organization) {
      setLoading(true);
      getOrganizations().then((values) => {
        console.log(values)
        if (values.find(value => value.id === organization)) {
          setLoading(false)
          return;
        }
        setOrganization(null)
        setLoading(false);
      });
    }
  }, [isAuthenticated, organization]);

  const getOrganizations = useCallback(async () => {
    const response = await authenticatedFetch('/organizations');
    return (await response.json()) as Organization[]
  }, [])

  return <SyncContext.Provider value={{ organization, isLoading }}>
    {children}
  </SyncContext.Provider>
}

export const useSyncContext = () => UseSelector.useContext(SyncContext)
export default SyncProvider;