import { ChakraProvider, Stack } from '@chakra-ui/react';
import AuthProvider from './contexts/AuthContext';
import ConnectionProvider from './contexts/ConnectionContext';
import AppRoutes from './router';

function App() {
  return (
    <ChakraProvider>
      <Stack>
        <ConnectionProvider>
          <AuthProvider>
            <AppRoutes/>
          </AuthProvider>
        </ConnectionProvider>
      </Stack>
    </ChakraProvider>
  )
}

export default App
