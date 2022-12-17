import React, { useEffect } from 'react';
import * as UseSelector from 'use-context-selector';
import { useState } from 'react';

type ConnectionContextType = {
  isOnline: boolean;
  isOffline: boolean;
};

const ConnectionContext = UseSelector.createContext<ConnectionContextType>(
  {} as ConnectionContextType
);

const ConnectionProvider: React.FC<React.PropsWithChildren> = ({ children }) => {
  const [isOnline, setOnline] = useState<boolean>(navigator.onLine);

  useEffect(() => {
    function update() {
      setOnline(navigator.onLine);
    }

    window.addEventListener('online', update);
    window.addEventListener('offline', update);

    return () => {
      window.removeEventListener('offline', update);
      window.removeEventListener('online', update);
    };
  }, []);

  return (
    <ConnectionContext.Provider value={{ isOnline, isOffline: !isOnline }}>
      {children}
    </ConnectionContext.Provider>
  );
};


export const useConnection = () => UseSelector.useContext(ConnectionContext);
export default ConnectionProvider;