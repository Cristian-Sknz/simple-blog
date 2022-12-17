import { Button, Heading, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, ModalProps } from '@chakra-ui/react';
import { faSitemap, faEnvelopeCircleCheck, faBriefcase } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface OrganizationModalProps extends Omit<ModalProps, 'children'> {
  onCreateClick?(): void
  onInviteJoinClick?(): void
  onExistentClick?(): void
}
 
const OrganizationModal: React.FC<OrganizationModalProps> = (props) => {
  return (
    <Modal size={{
      base: 'auto',
      sm: 'md'
    }} isCentered {...props}>
      <ModalContent w={'90%'} outlineColor={'twitter.700'} outline={'2px solid'} filter={'auto'} bg={'gray.800'}>
        <ModalHeader fontFamily={'Poppins'} color={'white'} textAlign={'center'}>
          <Heading size={'md'}>Conecte-se em uma organização!</Heading>
          <Heading fontWeight={'light'} my={2} size={'xs'}>
            Crie ou entre em organizações existentes
          </Heading>
        </ModalHeader>
        <ModalBody display={'flex'} flexDir={'column'} gap={1}>
          <Button 
            leftIcon={<FontAwesomeIcon icon={faSitemap} size={'lg'}/>} 
            fontFamily={'Montserrat'} h={'16'} 
            colorScheme={'orange'} 
            w={'full'}
            onClick={props.onCreateClick}
            fontSize={{
              base: '14px',
              sm: 'md'
            }}
          >
            Criar uma organização
          </Button>

          <Button 
            onClick={props.onInviteJoinClick}                      
            leftIcon={<FontAwesomeIcon icon={faEnvelopeCircleCheck} size={'lg'}/>} 
            fontFamily={'Montserrat'} h={'16'} 
            colorScheme={'orange'} 
            w={'full'}
            fontSize={{
              base: '14px',
              sm: 'md'
            }}
          >
            Entrar por convite
          </Button>

          <Button 
            onClick={props.onExistentClick}
            leftIcon={<FontAwesomeIcon icon={faBriefcase} size={'lg'}/>} 
            fontFamily={'Montserrat'} h={'16'} 
            colorScheme={'twitter'} 
            w={'full'}
            fontSize={{
              base: '14px',
              sm: 'md'
            }}
          >
            Organizações existentes
          </Button>
        </ModalBody>
        <ModalFooter/>
      </ModalContent>
    </Modal>
  );
}
 
export default OrganizationModal;