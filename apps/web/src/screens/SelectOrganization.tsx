import { Avatar, Button, Heading, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, Text, useDisclosure, VStack } from '@chakra-ui/react';
import Header from '@components/Header';
import OrganizationModal, { CreateOrganization, InviteOrganization } from '@components/OrganizationsModal';
import PostInput from '@components/PostInput';
import { faEnvelope } from '@fortawesome/free-regular-svg-icons';
import { faBriefcase, faEnvelopeCircleCheck, faSitemap } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface SelectOrganizationProps {
  
}
 
const SelectOrganization: React.FC<SelectOrganizationProps> = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  return (
    <VStack as={'main'} borderX={'1px solid white'} borderColor={'gray.500'} ml={'0 !important'} alignItems={'stretch'} flex={1}>
      <Header>
        Página inicial
      </Header>
      <PostInput/>
      <VStack m={'0 !important'} top={0} left={0} zIndex={50} position={'absolute'} backdropFilter={'auto'} backdropBlur={'5px'} w={'100vw'} h={'100vh'}>
        <OrganizationModal onInviteJoinClick={onOpen} onClose={() => null} isOpen />
        <CreateOrganization onClose={() => null} isOpen/>
        <InviteOrganization onClose={onClose} isOpen={isOpen} />
      </VStack>
    </VStack>
  );
}
 
export default SelectOrganization;