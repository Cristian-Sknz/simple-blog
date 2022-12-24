import { VStack, Image } from '@chakra-ui/react';
import React, { PropsWithChildren } from 'react';
import ReactSVG from '@assets/react.svg';
 
const LoadingScreen: React.FC<PropsWithChildren> = () => {
  return (<VStack w={'full'} h={'100vh'} bg={'gray.700'} justify={'center'}>
    <Image w={'6rem'} src={ReactSVG} mb={8} />
  </VStack>);
}
 
export default LoadingScreen;