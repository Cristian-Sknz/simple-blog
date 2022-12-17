import React from 'react';
import useSWR from 'swr'
import OrganizationList from './OrganizationList';
import {
  Heading,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalHeader,
  ModalOverlay,
  ModalProps,
  VStack,
  Link,
  theme
} from '@chakra-ui/react';
import { Organization } from '@/database';

interface ExistentOrganizationProps extends Omit<ModalProps, 'children'> {
  fetcher(value: '/organizations' | '/organizations/public'): Promise<Organization[]>
  onChoiceOrganization(organization: Organization): void
}

const ExistentOrganization: React.FC<ExistentOrganizationProps> = (props) => {
  const myorgs = useSWR('/organizations', props.fetcher, { fallbackData: [] })
  const puborgs = useSWR('/organizations/public', props.fetcher, { fallbackData: [] })

  const data = puborgs.data!!.filter((p) => !myorgs.data!!.find((m) => p.id === m.id))

  return (
    <Modal
      size={{ base: 'auto', sm: 'md' }}
      {...props}
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
          <Heading size={'md'}>Entrar em um Organização</Heading>
          <Heading fontWeight={'light'} size={'xs'} mt={2} maxW={'300px'}>
            Entre em organizações que você está ou organizações publicas
          </Heading>
          <ModalCloseButton />
        </ModalHeader>
        <ModalBody>
          <VStack  mb={2} position={'relative'} css={scrollbar() as any} overflow={'auto'} display={'flex'} flexDir={'column'} color={'white'} alignItems={'stretch'} maxH={'200px'}>
            <Link href={'#puborgs'} id={'myorgs'} pb={1} bg={'gray.800'} zIndex={5} top={0} position={'sticky'} as={'h4'} size={'sm'}>
              Suas organizações
            </Link>
            <OrganizationList onOrganizationClick={props.onChoiceOrganization} {...myorgs}/>

            <Link href={'#puborgs'} id={'puborgs'} pb={1} bg={'gray.800'} zIndex={5} top={0} position={'sticky'} as={'h4'} size={'sm'}>
              Organizações publicas
            </Link>
            <OrganizationList onOrganizationClick={props.onChoiceOrganization} emptyText={'Não há organizações publicas'} isLoading={puborgs.isLoading} data={data}/>
          </VStack>
        </ModalBody>
      </ModalContent>
    </Modal>
  );
};

const scrollbar = () => {
  return {
    '&::-webkit-scrollbar': {
      width: '4px',
    },
    '&::-webkit-scrollbar-track': {
      width: '6px',
    },
    '&::-webkit-scrollbar-thumb': {
      background: theme.colors.whatsapp[500],
      borderRadius: '24px',
    },
  }
}

export default ExistentOrganization;
