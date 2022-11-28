import { useCallback, useEffect } from 'react';
import { Navigate, To, useLocation } from 'react-router-dom';
import { AuthContext, useAuth as useAuthContext } from '@contexts/AuthContext';
import { useContextSelector } from 'use-context-selector';
export enum AccessType {
  ALL = 'ALL',
  Anonymous = 'Anonymous',
  Authenticated = 'Authenticated',
}

type WithAuthFunction = (acess: AccessType, element: JSX.Element, to?: To) => JSX.Element;

export function useAuth() {
  const isAuthenticated = useContextSelector(AuthContext, (value) => value.isAuthenticated);
  const flush = useContextSelector(AuthContext, (value) => value.flush);
  const location = useLocation();
  //const isAuthenticated = true;

  useEffect(() => {
    flush();
  }, [flush, location]);

  const withAuth: WithAuthFunction = useCallback((access, element, to) => {
    switch (access) {
      case AccessType.ALL: {
        return element;
      }
      case AccessType.Anonymous: {
        return (isAuthenticated) ? <Navigate to={to || '/app'}/> : element;
      }
      case AccessType.Authenticated: {
        return (isAuthenticated) ? element : <Navigate to={to || '/'}/>
      }
    }
  }, [isAuthenticated]);
  return { withAuth };
}