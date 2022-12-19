import React from 'react';
import Link from '@components/Link';
import ReactSVG from '@assets/react.svg';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHouse } from '@fortawesome/free-solid-svg-icons';
import { useAuth } from '@contexts/AuthContext';
import {
  Hide,
  HStack,
  Image,
  List,
  ListItem,
  Show,
  Stack,
  Text,
  VStack,
} from '@chakra-ui/react';
import UserPopover from './UserPopover';


const Sidebar: React.FC = () => {
  const { logout, user } = useAuth();

  return (
    <VStack
      w={{ base: '100%', sm: 'auto', lg: '20%' }}
      maxW={{ base: 'auto', lg: '270px' }}
      minW={{ base: 'auto', lg: '165px' }}
      alignItems={{ base: 'center', lg: 'stretch' }}
      p={{
        base: '0 .5rem',
        sm: '.5rem .8rem 1rem 0',
        lg: '0.5rem 2rem 0.75rem 0',
      }}
      h={{ base: 'auto', sm: '100vh' }}
      flexDirection={{ base: 'row', sm: 'column' }}
      bg={'gray.900'}
      as={'header'}
      justifyContent={'space-between'}
      position={{
        base: 'fixed',
        sm: 'sticky'
      }}
      top={{
        base: 'calc(100% - 44px)',
        sm: 0
      }}
      gap={{ base: 5, sm: 0 }}
      zIndex={5}
    >
      <HStack
        h={'2.5rem'}
        alignSelf={{ base: 'auto', lg: 'center' }}
      >
        <Image src={ReactSVG} />
        <Hide below='lg'>
          <Text ml={4} fontFamily={'Montserrat'} fontWeight={600} color={'white'}>
            Simple Blog
          </Text>
        </Hide>

        <Show below='sm'>
          <Text ml={4} fontFamily={'Montserrat'} fontWeight={600} color={'white'}>
            Simple Blog
          </Text>
        </Show>
      </HStack>

      <Stack mt={'0 !important'} flexDirection={{ base: 'row', sm: 'column'}} flex={{ base: 'none', sm: 1 }}>
        <List mt={{ base: 0, sm: 8 }}>
          <ListItem w={'100%'}>
            <Link
              replace
              _hover={{ bg: 'blue.400' }}
              gap={2}
              rounded={{ base: '50%', lg: 22 }}
              p={{ base: 1.5, sm: 2 }}
              to={'/app'}
            >
              <Stack mb={1}>
                <FontAwesomeIcon size='lg' icon={faHouse} color={'white'} />
              </Stack>
              <Hide below='lg'>
                <Text
                  fontWeight={600}
                  lineHeight={1}
                  as={'span'}
                  fontFamily={'Montserrat'}
                  color={'white'}
                >
                  Pagina inicial
                </Text>
              </Hide>
            </Link>
          </ListItem>
        </List>
      </Stack>
      <UserPopover onLogoutClick={logout} user={user}/>
    </VStack>
  );
};

export default Sidebar;