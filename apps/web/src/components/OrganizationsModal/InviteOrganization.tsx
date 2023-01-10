import React, { useEffect, useMemo } from 'react';
import { v4 } from 'uuid';
import * as yup from 'yup';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import useMutation from 'swr/mutation';
import { handleError } from '@/libs/fetch';
import InviteConsentModal from './InviteConsentModal';
import {
  Button,
  FormLabel,
  Heading,
  Input,
  InputGroup,
  List,
  ListItem,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  ModalProps,
  Text,
  VStack,
} from '@chakra-ui/react';

interface InviteOrganizationProps extends Omit<ModalProps, 'children'> {
  onOrganizationJoin?(organization: any): void
  fetcher: (value: `/invite/${string}`) => any
  poster: (value: `/invites/${string}/join`) => any
}

const InviteURL = `${window.location.protocol}//${window.location.host}/invite/`

const schema = yup.object({
  invite: yup.string()
    .transform((value: string) => value.startsWith(InviteURL) ? value.substring(InviteURL.length) : value)
    .uuid("Preencha um convite valido!")
    .required()
})


const InviteOrganization: React.FC<InviteOrganizationProps> = (props) => {
  const { register, reset, handleSubmit, ...form } = useForm({
    resolver: yupResolver(schema)
  })
  const mutation = useMutation('/invite', async (_, args) => {
    return props.fetcher(`/invites/${args['arg']}` as any)
      .then((response: any) => ({...response, invite: args['arg']}))
  });

  const uuid = useMemo(() => v4(), [props.isOpen])
  const error = mutation.error ? handleError(mutation.error, {}) : form.formState.errors?.invite?.message

  useEffect(() => {
    reset()
    mutation.reset()
  }, [props.isOpen])

  return (
    <Modal size={{ base: 'auto', sm: 'md' }} isCentered {...props} 
      isOpen={mutation.isMutating ? mutation.isMutating : props.isOpen}
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
          <Heading fontWeight={'light'} size={'xs'} mt={2} maxW={'300px'}>
            Insira um convite abaixo para entrar em uma organização existente
          </Heading>
          <ModalCloseButton/>
        </ModalHeader>
        <ModalBody display={'flex'} flexDir={'column'} gap={2} alignItems={'stretch'}>
          <FormLabel htmlFor={'invite'} display={'inline-block'} fontSize={'sm'} color={error ? 'red.600' :'white'} fontWeight={'bold'}>
            Insira o convite:
            <Text ml={1} fontWeight={'light'} display={'inline-block'} fontSize={error ? 'xs' : 'sm'} color={'red.600'}>
              <>{error ? error : '*'}</>
            </Text>
          </FormLabel>
          <InputGroup>
            <Input 
              color={'white'} 
              placeholder={InviteURL + uuid}
              isInvalid={!!error}
              id={'invite'}
              {...register('invite')}
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
                {InviteURL + uuid}
              </ListItem>
            </List>
          </VStack>
        </ModalBody>
        <ModalFooter justifyContent={'space-between'}>
          <Button isDisabled={mutation.isMutating} onClick={props.onClose} type='reset' colorScheme={'unstyled'}>
            Voltar
          </Button>
          <Button onClick={handleSubmit(async ({ invite }) => {
            mutation.trigger(invite);
          })} 
            type='submit' 
            colorScheme={'twitter'}
            isLoading={mutation.isMutating}
            isDisabled={!!form.formState.errors?.invite}
            loadingText={'Verificando'}
          >
            Entrar
          </Button>
        </ModalFooter>
        {mutation.data && (
          <InviteConsentModal 
            poster={props.poster} 
            organization={mutation.data} 
            onClose={mutation.reset}
            onOrganizationJoin={props.onOrganizationJoin}
          />
        )}
      </ModalContent>
    </Modal>
  );
};

export default InviteOrganization;