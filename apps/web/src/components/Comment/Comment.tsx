import React from 'react';
import { faDotCircle, faHeart } from '@fortawesome/free-regular-svg-icons';
import { Comment as DatabaseComment } from '@/database/model/entity';
import { snakeCase } from 'lodash';
import moment from 'moment';

import InteractionButton from '../Post/InteractionButton';
import { useEffectState, useObserverState } from '@hooks/useEffectState';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { HStack, Text, VStack, StackProps, Tooltip, Avatar } from '@chakra-ui/react';

interface CommentProps {
  _hstack?: StackProps;
  comment: DatabaseComment;
}
 
const Comment: React.FC<CommentProps> = (props) => {
  const user = useEffectState(() => props.comment.user.fetch());
  const likeCount = useObserverState(props.comment.likes.observeCount());
  
  return (
    <HStack alignItems={'flex-start'} {...props._hstack}>
      <HStack userSelect={'none'}>
        <Avatar name={user?.name} src={user?.image} w={12}/>
      </HStack>

      <VStack flex={1} py={2} alignItems={'stretch'}>
        <HStack alignItems={'center'} justifyContent={'flex-start'}>
          <Text fontWeight={500} color={'white'} fontFamily={'Poppins'} fontSize={'sm'}>
            {user?.name}
          </Text>
          <Text fontWeight={500} color={'white'} fontFamily={'Poppins'} fontSize={'xs'}>
            @{snakeCase(user?.username)}
          </Text>
        </HStack>

        <VStack alignItems={'flex-start'}>
          <Text fontSize={'sm'} color={'white'} fontFamily={'Montserrat'}>
            {props.comment.content}
          </Text>
        </VStack>

        <HStack alignItems={'stretch'} gap={2} justifyContent={'space-between'}>
          <InteractionButton onClick={(event) => {
            event.stopPropagation();
            props.comment.pushLike();
          }} hoverColor={'red.500'} color={'gray.200'} icon={faHeart}>
            {likeCount || 0}
          </InteractionButton>

          <Tooltip label={moment(props.comment.createdAt).format('LLL')}>
            <HStack px={2} rounded={8}>
              <FontAwesomeIcon color={'white'} size={'xs'} icon={faDotCircle} />
              <Text color={'white'} fontSize={'sm'}>
                {moment(props.comment.createdAt).calendar()}
              </Text>
            </HStack>
          </Tooltip>
        </HStack>
        
      </VStack>
    </HStack>
  );
}
 
export default Comment;