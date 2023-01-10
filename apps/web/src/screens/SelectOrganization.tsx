import React, { PropsWithChildren, useCallback } from 'react';
import { useDisclosure, VStack } from '@chakra-ui/react';

import { fetcher, poster } from '@/libs/fetch';
import Header from '@components/Header';
import PostInput from '@components/PostInput';
import { useOrganization } from '@contexts/OrganizationContext';
import { Organization } from '@/database';
import { Navigate } from 'react-router-dom';
import OrganizationModal, { 
  CreateOrganization, InviteOrganization, ExistentOrganization 
} from '@components/OrganizationsModal';

const Layout: React.FC<PropsWithChildren> = ({ children }) => {
  return (
    <VStack as={'main'} borderX={'1px solid white'} borderColor={'gray.500'} ml={'0 !important'} alignItems={'stretch'} flex={1}>
      <Header>
        Página inicial
      </Header>
      <PostInput/>
      <VStack m={'0 !important'} top={0} left={0} zIndex={50} position={'absolute'} backdropFilter={'auto'} backdropBlur={'5px'} w={'100vw'} h={'100vh'}>
        {children}
      </VStack>
    </VStack>
  )
}
 
const SelectOrganization: React.FC = () => {
  const { setOrganization, organization } = useOrganization();
  const organizations = useDisclosure({ isOpen: true })

  const create = useDisclosure();
  const invite = useDisclosure();
  const existent = useDisclosure();

  const onSelectOrganization = useCallback((value: Organization) => {
    localStorage.setItem('organization', JSON.stringify(value))
    setOrganization(value);
  }, []);

  if (organization) {
    return <Navigate to={'/app'}/>
  }

  return (
    <Layout>
      <OrganizationModal
        onCreateClick={create.onOpen}
        onInviteJoinClick={invite.onOpen}
        onExistentClick={existent.onOpen}
        {...organizations}
      />
      <CreateOrganization 
        poster={poster} 
        onClose={create.onClose} 
        isOpen={create.isOpen} 
        onOrganizationCreate={(created) => console.log(created)}
      />
      <InviteOrganization 
        fetcher={fetcher}
        poster={poster} 
        onClose={invite.onClose} 
        isOpen={invite.isOpen} 
        onOrganizationJoin={onSelectOrganization}
      />
      <ExistentOrganization 
        fetcher={fetcher}
        onClose={existent.onClose} 
        isOpen={existent.isOpen}
        onChoiceOrganization={onSelectOrganization}
      />
    </Layout>
  );
}
 
export default SelectOrganization;