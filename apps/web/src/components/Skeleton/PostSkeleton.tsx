import { HStack, SkeletonCircle, Avatar, VStack, SkeletonText, Heading, Tooltip, Text } from '@chakra-ui/react';
import InteractionButton from '@components/Post/InteractionButton';
import { faComment, faHeart, faDotCircle } from '@fortawesome/free-regular-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import moment from 'moment';
import React from 'react';

interface PostSkeletonProps {}

const PostSkeleton: React.FC<PostSkeletonProps> = () => {
  return (
    <HStack alignItems={'flex-start'}>
      <HStack userSelect={'none'}>
        <SkeletonCircle w={12} h={12}/>
      </HStack>

      <VStack flex={1} py={2} alignItems={'stretch'}>
        <HStack alignItems={'center'} justifyContent={'flex-start'}>
          <SkeletonText w={'30%'} skeletonHeight={'3'} maxW={'10rem'} noOfLines={1}/>
          <SkeletonText w={'20%'} skeletonHeight={'2'} maxW={'8rem'} noOfLines={1}/>
        </HStack>

        <VStack gap={2} wordBreak={'break-all'} alignItems={'flex-start'}>
          <SkeletonText w={'60%'} skeletonHeight={'6'} maxW={'15rem'} noOfLines={1}/>
          <SkeletonText w={'35%'} skeletonHeight={'3'} maxW={'12rem'} noOfLines={1}/>
          <SkeletonText w={'70%'} skeletonHeight={'2'} maxW={'40rem'} noOfLines={3}/>
        </VStack>

        <HStack justifyContent={'space-between'}>
          <HStack gap={2}>
            <InteractionButton
              hoverColor={'blue.200'}
              color={'gray.200'}
              icon={faComment}
            />
            <InteractionButton
              hoverColor={'red.500'}
              color={'gray.200'}
              icon={faHeart}
            />
          </HStack>
          <HStack px={2} rounded={8}>
            <FontAwesomeIcon color={'white'} size={'xs'} icon={faDotCircle} />
            <Text color={'white'} fontSize={'sm'}>
              <SkeletonText w={'4rem'} skeletonHeight={'3'} noOfLines={1}/>
            </Text>
          </HStack>
        </HStack>
      </VStack>
    </HStack>
  );
};

export default PostSkeleton;
