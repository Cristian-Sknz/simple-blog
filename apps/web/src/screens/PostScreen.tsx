import React from 'react';
import { database, Post } from '@/database';
import { Button, Text, VStack } from '@chakra-ui/react';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import Header from '@components/Header';
import PostWithComments from '@components/PostWithComments';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';
import { useObserverState } from '@hooks/useEffectState';
 
const PostScreen: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const post = useObserverState(database.get<Post>(Post.table).findAndObserve(id!!))
  const [params] = useSearchParams({
    comment: 'false',
  });

  return (
    <VStack as={'main'} borderX={'1px solid white'} borderColor={'gray.500'} ml={'0 !important'} alignItems={'stretch'} flex={1}>
      <Header _heading={{ gap: 5, alignItems: 'center'}}>
        <Button onClick={() => navigate(-1)} rounded={'50%'} px={0} colorScheme={'unstyled'} _hover={{ bg: 'gray.900'}}>
          <FontAwesomeIcon color={'white'} icon={faArrowLeft}/>
        </Button>
        <Text>Post</Text>
      </Header>
      {post && <PostWithComments post={post} focusInput={params.get('comment') === 'true'}/>}
    </VStack>
  );
}

export default PostScreen;