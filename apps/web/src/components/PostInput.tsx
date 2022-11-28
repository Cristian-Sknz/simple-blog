import React, { useCallback, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Button, FormControl, HStack, Image, Input, StackProps, Text, Textarea, VStack } from '@chakra-ui/react';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { database, Post } from '@/database';
import { useUser } from '@contexts/AuthContext';

type PostData = {
  title: string;
  subtitle?: string;
  content: string;
};
interface PostInputProps {
  _hstack?: StackProps;
}


const schema = yup.object({
  title: yup.string()
    .min(10, 'O titulo deve ter pelo menos 10 caracteres').required(),
  subtitle: yup.string().optional().nullable(),
  content: yup.string()
    .min(8, 'O conteudo do post tem que ter pelo menos 8 caracteres')
    .max(244, 'É permitido o maximo de 244 caracteres').required(),
});

 
const PostInput: React.FC<PostInputProps> = (props) => {
  const [isLoading, setLoading] = useState(false);
  const { register, handleSubmit, formState: { errors } } = useForm<PostData>({
    resolver: yupResolver(schema),
  });
  const user = useUser();

  const onSend = useCallback(async (newPost: any) => {
    try {
      setLoading(true);
      await database.write(async () => {
        await database.get<Post>(Post.table).create(post => {
          post.title = newPost.title;
          post.subtitle = newPost.subtitle;
          post.content = newPost.content;
          post.user.set(user);
        });
      })
    } finally {
      setLoading(false);
    }
  }, [user]);

  var error = Object.values(errors).find((error) => !!error?.message)?.message;

  return (
    <HStack alignItems={'flex-start'} {...props._hstack} px={2} pr={4}>
      <HStack>
        <Image
          w={12}
          h={12}
          rounded={'50%'}
          src={'https://xsgames.co/randomusers/avatar.php?g=female'}
        />
      </HStack>
      <VStack pt={2} alignItems={'flex-start'} flex={1}>
        <VStack alignItems={'stretch'} w={'full'}>
          <Input {...register('title')} px={2} h={'2rem'} color={'white'} placeholder='Titulo*'/>
          <Input {...register('subtitle')} px={2} h={'2rem'} color={'white'} placeholder='Subtitulo'/>
          <Textarea {...register('content', { maxLength: 244 })} px={2} h={'2rem'} resize={'none'} color={'white'} placeholder='Escreva um post*'/>
          <Text display={!!error ? 'inline-block' : 'none'} fontSize={'14px'} color={'red.400'}>
            {error}
          </Text>
        </VStack>
        <HStack alignSelf={'flex-end'} gap={2}>
          <Button 
            isLoading={isLoading}
            loadingText='Postando...' 
            onClick={handleSubmit(onSend)} 
            type='submit' 
            colorScheme={'blue'}
          >
            Postar
          </Button>
        </HStack>
      </VStack>
    </HStack>
  );
}
 
export default PostInput;