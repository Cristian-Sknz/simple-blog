import { Organization } from '@/database';
import { VStack, Spinner, Heading } from '@chakra-ui/react';
import OrganizationCard from './OrganizationCard';

interface OrganizationListProps {
  isLoading: boolean;
  data?: any[];
  emptyText?: string;
  onOrganizationClick(organization: Organization): void
}

const OrganizationList: React.FC<OrganizationListProps> = (props) => {
  return (
    <VStack px={2} fontFamily={'Poppins'} as={'ul'} alignItems={'stretch'}>
      {props.isLoading ? (
        <Spinner />
      ) : props.data!!.length == 0 ? (
        <Heading size={'sm'}>{props.emptyText || 'Você não está em nenhuma organização'}</Heading>
      ) : (
        props.data!!.map((value) => (
          <OrganizationCard 
            key={value.id} 
            as={'li'} 
            organization={value} 
            onClick={() => props.onOrganizationClick && props.onOrganizationClick(value)}
          />
        ))
      )}
    </VStack>
  );
};

export default OrganizationList;
