import React from 'react';
import {
  Button,
  ColorProps,
  Stack,
  SystemStyleObject,
  Text,
  Theme,
} from '@chakra-ui/react';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface InteractionButtonProps {
  icon: IconDefinition;
  color: ColorProps['color'];
  hoverColor: ColorProps['color'];
  onClick?: React.MouseEventHandler<HTMLButtonElement>;

  children: string | number;
}

const InteractionButton: React.FC<InteractionButtonProps> = (props) => {
  return (
    <Button
      _hover={{
        color: props.hoverColor,
      }}
      gap={1}
      p={0}
      className='group'
      color={props.color}
      h={'auto'}
      colorScheme={'unstyled'}
      alignItems={'center'}
      justifyContent={'center'}
      onClick={props.onClick}
    >
      <Stack
        _before={{
          _groupHover: {
            bg: props.hoverColor,
            opacity: 0.5,
            brightness: 1,
          },
          content: '""',
          p: 1.5,
          rounded: '50%',
          top: 0,
          left: 0,
          bottom: 0,
          right: 0,
          position: 'absolute',
          zIndex: 1,
          bg: 'transparent',
          transitionDuration: '150ms',
          filter: 'auto',
          blur: '2px',
          brightness: '170%'
        }}
        p={1.5}
        rounded={'50%'}
        position={'relative'}
      >
        <FontAwesomeIcon
          color={'inherit'}
          style={{ zIndex: 2 }}
          inverse={true}
          icon={props.icon}
          size={'lg'}
        />
      </Stack>
      <Text lineHeight={1} fontSize={'sm'} fontFamily={'Roboto'} as={'span'}>
        {props.children}
      </Text>
    </Button>
  );
};

export default InteractionButton;
