import React, { useEffect, useState } from 'react';
import * as UseSelector from 'use-context-selector';
import { fetcher } from '@/libs/fetch';
import { Organization } from '@/database';
import LoadingScreen from '@screens/LoadingScreen';
import SyncProvider from './SyncContext';

type OrganizationContextType = {
  organization: Organization | null;
  setOrganization(value: Organization): void
}

const OrganizationContext = UseSelector.createContext<OrganizationContextType>({} as OrganizationContextType)

type OrganizationProviderProps = {
  isAuthenticated: boolean;
  children: React.ReactNode
}

const OrganizationProvider: React.FC<OrganizationProviderProps> = ({ children, isAuthenticated }) => {
  const [organization, setOrganization] = useState<Organization | null>();

  useEffect(() => {
    const organization : Organization = JSON.parse(localStorage.getItem('organization') || '{}');
    if (!isAuthenticated) {
      setOrganization(null);
      return
    }

    if (organization?.id) {
      fetcher<Organization>('/organizations/' + organization.id )
        .then(setOrganization)
        .catch(() => {
          localStorage.removeItem('organization')
        });
    }
    setOrganization(null);
  }, [isAuthenticated]);

  return typeof organization == 'undefined' ? (
    <LoadingScreen/>
  ) : (
    <OrganizationContext.Provider value={{ organization, setOrganization }}>
      <SyncProvider>
        {children}
      </SyncProvider>
    </OrganizationContext.Provider>
  )
}

export const useOrganization = () => UseSelector.useContext(OrganizationContext)
export default OrganizationProvider;