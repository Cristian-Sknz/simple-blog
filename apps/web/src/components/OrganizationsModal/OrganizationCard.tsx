import { Avatar, Button, ButtonProps } from '@chakra-ui/react';

interface OrganizationCardProps extends Omit<ButtonProps, 'children' | 'onClick'> {
  organization: {
    name: string
  }
  onClick?(organization: OrganizationCardProps['organization']): void
}

const OrganizationCard: React.FC<OrganizationCardProps> = (props) => {
  return (
    <Button
      cursor={'pointer'}
      bg={'whiteAlpha.500'}
      rounded={8}
      p={2}
      onClick={() => props.onClick ? props.onClick(props.organization) : null}
      justifyContent={'flex-start'}
    >
      <Avatar size={'sm'} name={props.organization.name} mr={2} />
      {props.organization.name}
    </Button>
  );
};

export default OrganizationCard;
