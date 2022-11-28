import React from 'react';
import {
  Heading,
  HeadingProps,
  HStack,
} from '@chakra-ui/react';

interface HeaderProps extends React.PropsWithChildren {
  _heading?: HeadingProps;
}

const Header: React.FC<HeaderProps> = (props) => {
  return (
    <HStack
      zIndex={3}
      top={0}
      position={'sticky'}
      p={1}
      px={4}
      height={'3rem'}
      bg={'#0000009e'}
      backdropFilter={'auto'}
      backdropBlur={'5px'}
    >
      <Heading display={'flex'} fontFamily={'Montserrat'} size={'md'} color={'white'} {...props._heading}>
        {props.children}
      </Heading>
    </HStack>
  );
};

export default Header;
