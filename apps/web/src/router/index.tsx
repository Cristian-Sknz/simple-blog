import { Route, Routes } from 'react-router-dom'
import BlogFeed from '@screens/BlogFeed'
import { AccessType, useAuth } from './withAuth'
import LoginScreen from '@screens/LoginScreen';
import PageLayout from '@screens/PageLayout';
import PostScreen from '@screens/PostScreen';

const AppRoutes: React.FC = () => {
  const { withAuth } = useAuth();

  return (
    <Routes>
      <Route path='/'>
        <Route index element={withAuth(AccessType.Anonymous, <LoginScreen/>, '/app')}/>
        <Route path='app' element={withAuth(AccessType.Authenticated, <PageLayout/>)}>
          <Route index element={<BlogFeed/>}/>
          <Route path='post/:id' element={<PostScreen/>}/>
        </Route>
      </Route>
    </Routes>
  )
}

export default AppRoutes;