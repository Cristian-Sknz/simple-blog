import React, { useState } from 'react';
import moment from 'moment';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faComment, faDotCircle, faHeart } from '@fortawesome/free-regular-svg-icons';
import { Post as DatabasePost } from '@/database';
import { useEffectState, useObserverState } from '@/hooks/useEffectState';

import InteractionButton from './InteractionButton';
import {
  Heading,
  HStack,
  Image,
  SkeletonCircle,
  SkeletonText,
  Text,
  Tooltip,
  VStack,
} from '@chakra-ui/react';

interface PostProps {
  post: DatabasePost;
  onCommentClick?: React.MouseEventHandler<HTMLButtonElement>
}

const Post: React.FC<PostProps> = (props) => {
  const user = useEffectState(() => props.post.user.fetch());
  const comments = useObserverState(props.post.comments.observeCount());
  const likes = useObserverState(props.post.likes.observeCount());

  return (
    <HStack alignItems={'flex-start'}>
      <HStack>
        <SkeletonCircle w={12} h={12} isLoaded={!!user}>
          <Image w={12} h={12}
            rounded={'50%'}
            src={user?.image || 'https://xsgames.co/randomusers/avatar.php?g=male'}
          />
        </SkeletonCircle>
      </HStack>

      <VStack flex={1} py={2} alignItems={'stretch'} >
        <HStack alignItems={'center'} justifyContent={'flex-start'}>
          <SkeletonText skeletonHeight={3} minW={24} noOfLines={1} isLoaded={!!user}>
            <Text fontWeight={500} color={'white'} fontFamily={'Poppins'} fontSize={'sm'}>
              {user?.name}
            </Text>
          </SkeletonText>
          <SkeletonText noOfLines={1} isLoaded={!!user}>
            <Text fontWeight={500} color={'white'} fontFamily={'Poppins'} fontSize={'xs'}>
              @{user?.username}
            </Text>
          </SkeletonText>
        </HStack>

        <VStack wordBreak={'break-all'} alignItems={'flex-start'}>
          <Heading color={'white'} size={'lg'}>
            {props.post.title}
          </Heading>
          <Heading color={'white'} size={'sm'}>
            {props.post.subtitle}
          </Heading>
          <Text fontSize={'sm'} color={'white'} fontFamily={'Montserrat'}>
            {props.post.content}
          </Text>
        </VStack>

        <HStack justifyContent={'space-between'}>
          <HStack gap={2}>
            <InteractionButton onClick={props.onCommentClick} hoverColor={'blue.200'} color={'gray.200'} icon={faComment}>
              {comments || 0}
            </InteractionButton>
            <InteractionButton onClick={(event) => {
              event.stopPropagation();
              props.post.pushLike();
            }} hoverColor={'red.500'} color={'gray.200'} icon={faHeart}>
              {likes || 0}
            </InteractionButton>
          </HStack>
          <Tooltip label={moment(props.post.createdAt).format('LLL')}>
            <HStack px={2} rounded={8}>
              <FontAwesomeIcon color={'white'} size={'xs'} icon={faDotCircle} />
              <Text color={'white'} fontSize={'sm'}>
                {moment(props.post.createdAt).calendar()}
              </Text>
            </HStack>
          </Tooltip>
        </HStack>
      </VStack>
    </HStack>
  );
};

export default Post;
