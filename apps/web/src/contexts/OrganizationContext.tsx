import React, { useEffect, useState } from 'react';
import * as UseSelector from 'use-context-selector';
import { fetcher } from '@/libs/fetch';
import { Organization } from '@/database';
import { v4 } from 'uuid';

type OrganizationContextType = {
  organization: Organization | null;
  isLoading: boolean
}

const OrganizationContext = UseSelector.createContext<OrganizationContextType>({} as OrganizationContextType)

type OrganizationProviderProps = {
  isAuthenticated: boolean;
  children: React.ReactNode
}

const OrganizationProvider: React.FC<OrganizationProviderProps> = ({ children, isAuthenticated }) => {
  const [organization, setOrganization] = useState<Organization | null>(null);
  const [isLoading, setLoading] = useState<boolean>(true)

  useEffect(() => {
    if (!isAuthenticated) {
      setLoading(false)
      return
    }

    const organization = localStorage.getItem('organization') || v4();

    if (organization) {
      setLoading(true);
      fetcher<Organization>('/organizations/' + organization )
        .then(setOrganization)
        .catch((err) => {
          console.log(err)
          localStorage.removeItem('organization')
        })
        .finally(() => setLoading(false))
    }
  }, [isAuthenticated]);

  return (
    <OrganizationContext.Provider value={{ organization, isLoading }}>
      {children}
    </OrganizationContext.Provider>
  )
}

export const useOrganization = () => UseSelector.useContext(OrganizationContext)
export default OrganizationProvider;