import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { BlogUser } from '@/database';
import { faEllipsis } from '@fortawesome/free-solid-svg-icons';
import {
  Avatar,
  Button,
  Hide,
  HStack,
  Image,
  Popover,
  PopoverArrow,
  PopoverBody,
  PopoverContent,
  PopoverHeader,
  PopoverTrigger,
  Text,
  VStack,
} from '@chakra-ui/react';

interface UserPopoverProps {
  user: BlogUser | null;
  onLogoutClick(): Promise<void> | void;
}
 
const UserPopover: React.FC<UserPopoverProps> = ({ user, ...rest }) => {
  return (
    <Popover colorScheme={'unstyled'} computePositionOnMount>
    <PopoverTrigger>
      <HStack
        _hover={{ bg: 'blue.600' }}
        transitionDuration={'150ms'}
        cursor={'pointer'}
        justifyContent={'space-between'}
        mt={'0 !important'}
        rounded={{
          base: '50%',
          lg: 18,
        }}
        py={{
          base: 1,
          lg: 2,
        }}
        px={{
          base: 1,
          lg: 3,
        }}
      >
        <HStack>
          <Avatar userSelect={'none'} name={user?.name} src={user?.image} w={9} h={9}/>
          <Hide below='lg'>
            <VStack alignItems={'flex-start'} lineHeight={1}>
              <Text fontFamily={'Poppins'} fontWeight={500} lineHeight={1} color={'white'}>
                {user?.name}
              </Text>
              <Text
                fontFamily={'Poppins'}
                fontSize={'sm'}
                mt={'0 !important'}
                lineHeight={1}
                color={'gray.300'}
              >
                @{user?.username}
              </Text>
            </VStack>
          </Hide>
        </HStack>
        <Hide below='lg'>
          <FontAwesomeIcon icon={faEllipsis} color={'white'} />
        </Hide>
      </HStack>
    </PopoverTrigger>
    <PopoverContent px={0} maxW={'300px'} borderColor={'gray.700'} bg={'gray.800'}>
      <PopoverArrow bg={'gray.800'} borderColor={'gray.700'} />
      <PopoverHeader borderColor={'gray.700'}/>
      <PopoverBody alignItems={'stretch'} color={'white'}>
        <Button 
          _hover={{
            bg: 'gray.600'
          }} 
          justifyContent={'flex-start'} 
          colorScheme={'unstyled'} 
          w={'full'}
        >
          Mudar de organização
        </Button>
        <Button 
          _hover={{
            bg: 'red.600'
          }} 
          justifyContent={'flex-start'} 
          colorScheme={'unstyled'} 
          w={'full'}
          onClick={rest.onLogoutClick}
        >
          Sair de @{user?.username}
        </Button>
      </PopoverBody>
    </PopoverContent>
  </Popover>
  );
}
 
export default UserPopover;