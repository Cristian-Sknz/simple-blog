import React, { useEffect, useState } from 'react';
import * as UseSelector from 'use-context-selector';
import { fetcher } from '@/libs/fetch';
import { Organization } from '@/database';
import { v4 } from 'uuid';

type OrganizationContextType = {
  organization: Organization | null;
  setOrganization(value: Organization): void
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
    const organization : Organization = JSON.parse(localStorage.getItem('organization') || '{}');
    if (!isAuthenticated) {
      setLoading(false);
      return
    }

    if (organization?.id) {
      fetcher<Organization>('/organizations/' + organization.id )
        .then(setOrganization)
        .catch(() => {
          localStorage.removeItem('organization')
        })
        .finally(() => setLoading(false))
    } else {
      setLoading(false);
    }
  }, [isAuthenticated]);

  return (
    <OrganizationContext.Provider value={{ organization, setOrganization, isLoading }}>
      {children}
    </OrganizationContext.Provider>
  )
}

export const useOrganization = () => UseSelector.useContext(OrganizationContext)
export default OrganizationProvider;