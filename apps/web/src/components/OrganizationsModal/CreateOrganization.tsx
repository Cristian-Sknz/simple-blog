import { Modal, ModalOverlay, ModalContent, ModalHeader, Heading, ModalCloseButton, ModalBody, InputGroup, Input, VStack, List, ListItem, ModalFooter, Button, InputLeftAddon, Avatar, ModalProps, Radio, RadioGroup, TagLabel, Text, Checkbox } from '@chakra-ui/react';
import { useState, useMemo, useEffect } from 'react';
import { v4 } from 'uuid';

interface CreateOrganizationProps extends Omit<ModalProps, 'children'> {
  
}
 
const CreateOrganization: React.FC<CreateOrganizationProps> = (props) => {
  const [loading, setLoading] = useState<boolean>(false);

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
          <Heading size={'md'}>Crie uma organização</Heading>
          <Heading fontWeight={'light'} size={'xs'} my={2} maxW={'300px'}>
            Insira um nome e declare a visibilidade
          </Heading>
          <ModalCloseButton/>
        </ModalHeader>
        <ModalBody display={'flex'} flexDir={'column'} gap={2} alignItems={'stretch'}>
          <InputGroup>
            <InputLeftAddon bg={'gray.800'}>
              <Avatar bg={'purple.500'} size={'sm'} name={'Nome do Blog'}/>
            </InputLeftAddon>
            <Input 
              color={'white'} 
              placeholder={`Nome do blog`}
            />
          </InputGroup>
          <Checkbox alignSelf={'flex-end'} color={'white'}>Publico</Checkbox>
        </ModalBody>
        <ModalFooter justifyContent={'space-between'}>
          <Button isDisabled={loading} onClick={props.onClose} type='reset' colorScheme={'unstyled'}>
            Voltar
          </Button>
          <Button 
            type='submit' 
            colorScheme={'twitter'}
            isLoading={loading}
            loadingText={'Verificando'}
          >
            Criar
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  )
}
 
export default CreateOrganization;