import { ChakraProvider } from '@chakra-ui/react';
import AuthProvider from './contexts/AuthContext';
import ConnectionProvider from './contexts/ConnectionContext';
import AppRoutes from './router';

function App() {
  return (
    <ChakraProvider>
      <ConnectionProvider>
        <AuthProvider>
          <AppRoutes/>
        </AuthProvider>
      </ConnectionProvider>
    </ChakraProvider>
  )
}

export default App
