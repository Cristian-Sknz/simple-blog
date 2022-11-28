import React from 'react';
import { Card, VStack } from '@chakra-ui/react';
import { Post as DatabasePost } from '@/database/model/entity';

import Post from './Post';
import Comment, { CommentInput } from './Comment';
import { useObserverState } from '@/hooks/useEffectState';
import { useEffect } from 'react';
import { useRef } from 'react';

interface PostWithCommentsProps {
  post: DatabasePost;

  focusInput?: boolean;
}
 
const PostWithComments: React.FC<PostWithCommentsProps> = (props) => {
  const comments = useObserverState(props.post.comments.observe());
  const ref = useRef<HTMLTextAreaElement>(null);
  useEffect(() => {
    if (props.focusInput) {
      ref.current?.focus();
    }
  }, [props.focusInput]);

  return (
    <Card py={2}>
      <Card rounded={0} as={'article'} px={2} borderBottomWidth={1} borderColor={'gray.500'}>
        <Post post={props.post} onCommentClick={() => ref.current?.focus()}/>
      </Card>
      <VStack alignItems='stretch' my={2}>
        <CommentInput ref={ref} post={props.post}/>
      </VStack>
      <VStack alignItems='stretch' _last={{ borderBottomWidth: 1, borderColor: 'gray.500'}}>
        {comments?.map(comment => (
          <Card 
            key={comment.id}
            mt={'0 !important'}
            as={'article'} 
            alignItems={'stretch'} 
            borderTopWidth={1}
            borderColor={'gray.500'}
            p={2} 
          >
            <Comment comment={comment}/>
          </Card>
        ))}
      </VStack>
    </Card>
  );
}
 
export default PostWithComments;