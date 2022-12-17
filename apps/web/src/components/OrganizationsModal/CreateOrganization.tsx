import React, { useEffect, useState } from 'react';
import { InferType } from 'yup';
import * as yup from 'yup';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import useMutation from 'swr/mutation';
import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  Heading,
  ModalCloseButton,
  ModalBody,
  InputGroup,
  Input,
  ModalFooter,
  Button,
  InputLeftAddon,
  Avatar,
  ModalProps,
  Checkbox,
} from '@chakra-ui/react';
import { Organization } from '@/database';


interface CreateOrganizationProps<T extends Organization = Organization> extends Omit<ModalProps, 'children'> {
  poster: (value: `/organizations`) => Promise<T>;
  onOrganizationCreate(organization: Organization): void
}

const schema = yup.object({
  name: yup.string().required(),
  public: yup.boolean().required()
})

const CreateOrganization: React.FC<CreateOrganizationProps> = (props) => {
  const { register, reset, watch, handleSubmit } = useForm({
    resolver: yupResolver(schema),
  });
  const mutation = useMutation('/organizations', props.poster)

  useEffect(() => {
    reset();
    mutation.reset();
  }, [props.isOpen])

  return (
    <Modal
      size={{ base: 'auto', sm: 'md' }}
      isCentered
      {...props}
      isOpen={mutation.isMutating ? true : props.isOpen}
    >
      <ModalOverlay />
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
          <ModalCloseButton />
        </ModalHeader>
        <ModalBody display={'flex'} flexDir={'column'} gap={2} alignItems={'stretch'}>
          <InputGroup>
            <InputLeftAddon bg={'gray.800'}>
              <Avatar bg={'purple.500'} size={'sm'} name={watch('name') || 'N B'} />
            </InputLeftAddon>
            <Input {...register('name')} color={'white'} placeholder={`Nome do blog`} />
          </InputGroup>
          <Checkbox {...register('public')} alignSelf={'flex-end'} color={'white'}>
            Publico
          </Checkbox>
        </ModalBody>
        <ModalFooter justifyContent={'space-between'}>
          <Button
            isDisabled={mutation.isMutating}
            onClick={props.onClose}
            type='reset'
            colorScheme={'unstyled'}
          >
            Voltar
          </Button>
          <Button
            type='submit'
            colorScheme={'twitter'}
            isLoading={mutation.isMutating}
            loadingText={'Criando'}
            onClick={handleSubmit((e) => {
              mutation.trigger(e, {
                onSuccess(data) {
                  props.onOrganizationCreate(data as any)
                },
              })
            })}
          >
            Criar
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};

export default CreateOrganization;
