import { Route, Routes } from 'react-router-dom'
import BlogFeed from '@screens/BlogFeed'
import PermissiveRoute, { AccessType } from './withAuth'
import LoginScreen from '@screens/LoginScreen';
import PageLayout from '@screens/PageLayout';
import PostScreen from '@screens/PostScreen';
import RouteElement from './withAuth';
import { useContextSelector } from 'use-context-selector';
import { AuthContext } from '@contexts/AuthContext';
import SelectOrganization from '@screens/SelectOrganization';

const AppRoutes: React.FC = () => {
  const isAuth = useContextSelector(AuthContext, (value) => value.isAuthenticated);

  return (
    <Routes>
      <Route path='/'>
        <Route index element={(
            <RouteElement access={AccessType.Anonymous} redirect='/app'>
              <LoginScreen/>
            </RouteElement>
          )}
        />
        
        <Route path='app' element={(
          <RouteElement access={AccessType.NEED_ORGANIZATION}>
            <PageLayout/>
          </RouteElement>
          )}
        >
          <Route index element={<BlogFeed/>}/>
          <Route path='post/:id' element={<PostScreen/>}/>
        </Route>

        <Route path='app/organizations' element={(
          <RouteElement access={AccessType.Authenticated}>
            <PageLayout/>
          </RouteElement>
          )}>
            <Route index element={<SelectOrganization/>}/>
        </Route>
      </Route>
    </Routes>
  )
}

export default AppRoutes;