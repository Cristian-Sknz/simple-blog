import { Button, Heading, Input, InputGroup, List, ListItem, Modal, ModalBody, ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, ModalProps, VStack } from '@chakra-ui/react';
import React, { useEffect, useMemo, useState } from 'react';
import { v4 } from 'uuid';

interface InviteOrganizationProps extends Omit<ModalProps, 'children'> {
  onJoinPress?(invite: string): Promise<boolean>
}

const InviteOrganization: React.FC<InviteOrganizationProps> = (props) => {
  const [invite, setInvite] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const uuid = useMemo(() => v4(), [])

  useEffect(() => {
    setInvite('');
  }, [props.isOpen])

  return (
    <Modal size={{ base: 'auto', sm: 'md' }} isCentered {...props} 
      isOpen={loading ? loading : props.isOpen}
    >
      <ModalOverlay/>
      <ModalContent
        w={'90%'}
        outlineColor={'twitter.700'}
        outline={'2px solid'}
        filter={'auto'}
        bg={'gray.800'}
      >
        <ModalHeader
          display={'flex'}
          flexDir={'column'}
          fontFamily={'Poppins'}
          color={'white'}
          alignItems={'center'}
          textAlign={'center'}
        >
          <Heading size={'md'}>Entrar em um Organização</Heading>
          <Heading fontWeight={'light'} size={'xs'} my={2} maxW={'300px'}>
            Insira um convite abaixo para entrar em uma organização existente
          </Heading>
          <ModalCloseButton/>
        </ModalHeader>
        <ModalBody display={'flex'} flexDir={'column'} gap={2} alignItems={'stretch'}>
          <InputGroup>
            <Input 
              color={'white'} 
              placeholder={`http://simpleblog.com/${uuid}`}
              onChange={event => setInvite(event.currentTarget.value)}
              value={invite}
            />
          </InputGroup>
          <VStack alignItems={'stretch'}>
            <Heading textAlign={'left'} fontWeight={'semibold'} color={'gray.300'} size={'sm'}>
              Convites normalmente são assim:
            </Heading>
            <List fontSize={'12px'} color={'gray.200'}>
              <ListItem py={'1px'} px={2} rounded={8} display={'inline-block'} bg={'red.800'}>
                {uuid}
              </ListItem>
              <ListItem mt={1} py={'1px'} px={2} rounded={8} display={'inline-block'} bg={'red.800'}>
                {`http://simpleblog.com/${uuid}`}
              </ListItem>
            </List>
          </VStack>
        </ModalBody>
        <ModalFooter justifyContent={'space-between'}>
          <Button isDisabled={loading} onClick={props.onClose} type='reset' colorScheme={'unstyled'}>
            Voltar
          </Button>
          <Button onClick={props.onJoinPress && (async () => {
            try {
              setLoading(true);
              await props.onJoinPress!!(invite);
            } catch (err) {
              console.error(err)
            } finally {
              setLoading(false);
            }
          })} 
            type='submit' 
            colorScheme={'twitter'}
            isLoading={loading}
            loadingText={'Verificando'}
          >
            Entrar
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};

export default InviteOrganization;