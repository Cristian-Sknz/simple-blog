import { Button, Card, Heading, Hide, HStack, Input, InputGroup, InputLeftAddon, InputRightAddon, List, Stack, VStack } from '@chakra-ui/react';
import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from '@components/Sidebar';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import PopularPosts from '@components/PopularPosts';

interface PageLayoutProps {}

const PageLayout: React.FC<PageLayoutProps> = () => {
  return (
    <Stack alignItems={'center'} bg={'gray.900'}>
      <HStack flexDirection={{
        base: 'column-reverse',
        sm: 'row'
      }} px={{ base: 0, sm: 3, lg: 8 }} minH={{ base: '100vh', sm: 'auto' }} w={'100%'} maxW={'1400px'} alignItems={'stretch'}>
        <Sidebar/>
        <Outlet />
        <PopularPosts />
      </HStack>
    </Stack>
  );
};

export default PageLayout;
