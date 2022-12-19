import React, { useCallback, useState } from 'react';
import { Post } from '@/database';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';

import { Avatar, Button, HStack, StackProps, Textarea, VStack } from '@chakra-ui/react';
import { useUser } from '@contexts/AuthContext';

interface CommentInputProps {
  _hstack?: StackProps;
  post: Post;
}

type CommentInputFC = React.ForwardRefRenderFunction<HTMLTextAreaElement, CommentInputProps>;

const schema = yup.object({
  content: yup.string()
    .min(4, 'Escreva no minimo 4 caracteres')
    .max(240, 'Maximo de 240 caracteres!')
    .required()
})
 
const CommentInput: CommentInputFC = (props, ref) => {
  const { register, handleSubmit, formState: { errors: { content } } } = useForm({
    resolver: yupResolver(schema),
  });
  const [isLoading, setLoading] = useState(false);
  const user = useUser()

  const onSend = useCallback(async ({ content }: any) => {
    try {
      setLoading(true);
      await props.post.addComment(content);
    } finally {
      setLoading(false);
    }
  }, []);

  const contentInput = register('content', { maxLength: 240});

  return (
    <HStack px={2} alignItems={'flex-start'} {...props._hstack}>
      <HStack userSelect={'none'}>
        <Avatar name={user?.name} src={user?.image} w={12}/>
      </HStack>
      <VStack pt={2} align={'flex-start'} flex={1}>
        <HStack w={'100%'}>
          <Textarea 
            h={'2rem'} 
            resize={'none'} 
            color={'white'}
            placeholder='O que pensa dessa postagem?'
            isInvalid={!!content}
            fontFamily={'Montserrat'}
            {...contentInput}
            ref={(value) =>  {
              contentInput.ref(value)
              if (!!ref) (ref as any).current = value
            }}
          />
        </HStack>
        <HStack alignSelf={'flex-end'} gap={2}>
          <Button 
            isDisabled={!!content} 
            isLoading={isLoading}
            onClick={handleSubmit(onSend)} 
            colorScheme={'blue'}
          >
              Comentar
          </Button>
        </HStack>
      </VStack>
    </HStack>
  );
}
 
export default React.forwardRef(CommentInput);