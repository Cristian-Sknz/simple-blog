import { Link as RouterLink } from 'react-router-dom';

import { chakra } from '@chakra-ui/react'

const Link = chakra(RouterLink, {
  label: 'AppLink',
  baseStyle: {
    _hover: { textDecor: 'none' },
    flexDirection: 'row',
    alignItems: 'center',
    display: 'flex' 
  }
})

export default Link;