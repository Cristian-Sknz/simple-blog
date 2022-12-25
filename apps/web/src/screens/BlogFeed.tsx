import React from 'react';
import { VStack } from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';
import { Q } from '@nozbe/watermelondb';

import Post from '@/components/Post';
import PostInput from '@components/PostInput';
import Card from '@components/Post/Card';
import Header from '@components/Header';
import { database, Post as DatabasePost } from '@/database';
import { useObserverState } from '@hooks/useEffectState';
 
const BlogFeed: React.FC = () => {
  const posts = useObserverState(database.get<DatabasePost>('posts')
    .query(Q.sortBy('created_at', 'desc'))
    .observe())
  const navigate = useNavigate();

  return (
    <VStack as={'main'} borderX={'1px solid white'} borderColor={'gray.500'} ml={'0 !important'} alignItems={'stretch'} flex={1}>
      <Header>
        Página inicial
      </Header>
      <VStack alignItems={'stretch'}>
        <PostInput _hstack={{ w: '100%', mb: 3 }}/>
        <VStack alignItems='stretch'>
          {posts?.map(post => (
            <Card 
              key={post.id}
              onClick={() => navigate(`post/${post.id}`)}
            >
              <Post post={post} onCommentClick={(event) => {
                event.stopPropagation();
                navigate(`post/${post.id}?comment=true`)
              }}/>
            </Card>
          ))}
        </VStack>
      </VStack>
    </VStack>
  );
}
 
export default BlogFeed;