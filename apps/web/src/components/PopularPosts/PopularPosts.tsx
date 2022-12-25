import React from 'react';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Q } from '@nozbe/watermelondb';
import { database, Post } from '@/database';
import { Card, Heading, Hide, Input, InputGroup, InputLeftAddon, List, SkeletonText, Text, Tooltip, VStack } from '@chakra-ui/react';
import { useObserverState } from '@hooks/useEffectState';
import Link from '@components/Link';

interface PopularPostsProps {}

const PopularPosts: React.FC<PopularPostsProps> = () => {
  const posts = useObserverState(database.collections.get<Post>(Post.table).query(Q.sortBy('created_at', 'desc')).observe())

  return (
    <Hide below={'md'}>
      <VStack
        w={{
          base: '30%',
          lg: '100%',
        }}
        maxW={{
          base: 'auto',
          lg: '300px'
        }}
        p={{
          base: '0.35rem 0 0.75rem 1rem',
        }}
        alignItems={{
          lg: 'stretch',
        }}
        h={'100vh'}
        bg={'gray.900'}
        as={'header'}
        position={'sticky'}
        top={0}
        ml={'0 !important'}
        gap={3}
      >
        <InputGroup h={'2.5rem'}>
          <InputLeftAddon borderRight={'none'} bg={'gray.900'} rounded={18}>
            <FontAwesomeIcon color={'white'} icon={faSearch} />
          </InputLeftAddon>
          <Input pl={2} borderLeft={'none'} placeholder='Pesquise no blog' rounded={18} />
        </InputGroup>

        <Card gap={4} bg={'gray.700'} alignItems={'stretch'} py={2} px={0}>
          <Heading
            fontFamily={'Poppins'}
            textAlign={'center'}
            size={'md'}
            color={'white'}
            as={'h4'}
          >
            Posts populares
          </Heading>

          <List w={'90%'} alignSelf={'center'} rounded={8} h={'270px'} >
            {posts?.map((post) => (
              <Tooltip key={post.id} bg={'gray.900'} rounded={8} placement={'top'} label={post.title}>
                <VStack as={'li'} fontSize={'sm'} _hover={{ bg: 'gray.600'}} alignItems={'stretch'} lineHeight={1.2} rounded={8} py={2} px={1}>
                  <Link display={'flex'} alignItems={'flex-start'} flexDir={'column'} to={`post/${post.id}`}>
                    <Text fontFamily={'Poppins'} fontWeight={500} color={'white'} noOfLines={1}>
                      {post.title}
                    </Text>
                    <Text fontFamily={'Montserrat'} mt={'0 !important'} color={'white'} noOfLines={1}>{post.subtitle}</Text>
                  </Link>
                </VStack>
              </Tooltip>
            ))}
          </List>
        </Card>
      </VStack>
    </Hide>
  );
};

export default PopularPosts;
