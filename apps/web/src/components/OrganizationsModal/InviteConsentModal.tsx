import React, { useCallback } from 'react';
import useMutation from 'swr/mutation';
import { handleError } from '@/libs/fetch';
import moment from 'moment';
import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  Heading,
  ModalCloseButton,
  ModalBody,
  VStack,
  ModalFooter,
  Button,
  ModalProps,
  HStack,
  Avatar,
  Text,
} from '@chakra-ui/react';

interface InviteConsentModalProps extends Omit<ModalProps, 'children' | 'isOpen'> {
  organization: any;
  poster: (value: `/invites/${string}/join`) => any;
  onOrganizationJoin?: (Organization: any) => void;
}
 
const InviteConsentModal: React.FC<InviteConsentModalProps> = (props) => {
  const { isMutating, trigger, error } = useMutation('/invites', (path, args) => {
    return props.poster(`${path}/${args['arg'].invite}/join` as any).then(() => args['arg'])
  });

  const onJoin = useCallback(() => {
    trigger(props.organization, {
      onSuccess(data) {
        if (props.onOrganizationJoin) props.onOrganizationJoin(data);
      },
    });
  }, [props.organization]);


  return (
    <Modal
      size={{ base: 'auto', sm: 'md' }}
      {...props}
      isOpen={!!props.organization}
      isCentered
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
          <Heading size={'md'}>{props.organization.name}</Heading>
          <Heading fontWeight={'light'} size={'xs'} mt={2} maxW={'300px'}>
            Você deseja entrar nesta organização?
          </Heading>
          <ModalCloseButton />
        </ModalHeader>
        <ModalBody>
          <VStack mb={2} overflow={'auto'} display={'flex'} color={'white'} alignItems={'stretch'}>
            <Text ml={1} fontWeight={'light'} display={'inline-block'} fontSize={error ? 'xs' : 'sm'} color={'red.600'}>
              <>{handleError(error, {})}</>
            </Text>
            <HStack spacing={0} bg={'gray.500'} rounded={16} p={2}>
              <Avatar size={'md'} name={props.organization.name} mr={2} />
              <HStack flex={1} justifyContent={'space-between'}>
                <VStack alignItems={'flex-start'} spacing={0}>
                  <Heading size={'sm'}>{props.organization.name}</Heading>
                  <Text fontWeight={'light'}>{props.organization.members.length} membros</Text>
                </VStack>

                <Text alignSelf={'flex-end'} fontSize={'sm'} fontWeight={'light'}>{moment(props.organization.createdAt).format('DD/MM/yyyy')}</Text>
              </HStack>
            </HStack>
          </VStack>
        </ModalBody>
        <ModalFooter>
          <Button onClick={onJoin} isLoading={isMutating} loadingText={'Entrando'} colorScheme={'twitter'}>
            Entrar
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
}
 
export default InviteConsentModal;