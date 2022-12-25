import React from 'react';
import { Card as ChakraCard, CardProps } from '@chakra-ui/react';
 
const Card: React.FC<CardProps> = (props) => {
  return (<ChakraCard 
    _hover={{ bg: 'gray.700' }}
    _focusVisible={{ outlineStyle: 'solid', outlineWidth: 2, outlineColor: 'twitter.600'}}
    mt={'0 !important'}
    as={'article'} 
    alignItems={'stretch'} 
    transition={'250ms'} 
    cursor={'pointer'}
    borderTopWidth={1}
    borderColor={'gray.500'}
    p={2}
    tabIndex={0}
    {...props}
  />);
}
 
export default Card;