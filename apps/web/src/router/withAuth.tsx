import { Navigate } from 'react-router-dom';
import { AuthContext } from '@contexts/AuthContext';
import { useContextSelector } from 'use-context-selector';
import { useOrganization } from '@contexts/OrganizationContext';

export enum AccessType {
  ALL = 'ALL',
  Anonymous = 'Anonymous',
  Authenticated = 'Authenticated',
  NEED_ORGANIZATION = "Need Organization"
}

interface RouteElementProps {
  /**
   * Se é uma rota que precisa estar autenticado.
   * 
   * @default true
   */
  access?: AccessType;

  /**
   * Especificar qual rota ele será redirecionado caso não estiver autenticado.
   * 
   * @default '/'
   */
  redirect?: string
  children: React.ReactNode
}

const RouteElement: React.FC<RouteElementProps> = (props) => {
  const { isLoading, organization } = useOrganization()
  const isAuthenticated = useContextSelector(AuthContext, (value) => value.isAuthenticated);

  switch(props.access || AccessType.ALL) {
    case AccessType.Anonymous: {
      if (isAuthenticated) return <Navigate to={props.redirect || '/'}/>
      break
    }
    case AccessType.Authenticated: {
      if (isAuthenticated) break;
      return <Navigate to={props.redirect || '/'}/>
    }
    case AccessType.NEED_ORGANIZATION: {
      if (!isAuthenticated) {
        return <Navigate to={props.redirect || '/'}/>
      }

      if (!organization && !isLoading) {
        return <Navigate to={props.redirect || '/app/organizations'}/>
      }
      break
    }
    case AccessType.ALL: {
      break
    }
  }

  return <>{props.children}</>
}

export default RouteElement;