import React, { useCallback, useState } from 'react';
import ReactSVG from '@assets/react.svg';
import { motion } from 'framer-motion';
import { useForm } from 'react-hook-form';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser } from '@fortawesome/free-regular-svg-icons';
import { faLock } from '@fortawesome/free-solid-svg-icons';
import { Link } from 'react-router-dom';
import { SignInData, useAuth } from '@contexts/AuthContext';

import { 
  Card, 
  Heading, 
  HStack, 
  VStack, 
  Image, 
  Input, 
  Text,
  InputGroup, 
  InputLeftAddon, 
  Button
} from '@chakra-ui/react';
import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';

const schema = yup.object({
  username: yup.string()
  .test('user', "Preencher o username/email corretamente!", (value) => (
    yup.string()
      .matches(/^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$/)
      .isValidSync(value) || 
    yup.string()
      .email()
      .isValidSync(value)
    )
  ).required("Preencha o username/email corretamente"),
  password: yup.string()
    .min(8, 'Você precisa inserir uma senha de 8 caracteres')
    .required('Preencha o campo e senha')
});


const LoginScreen: React.FC<any> = () => {
  const { register, formState: { errors }, handleSubmit, setError } = useForm<SignInData>({
    resolver: yupResolver(schema)
  });
  const [loading, setLoading] = useState<boolean>(false);
  const { signIn } = useAuth();

  const onLogin = useCallback((data: SignInData) => {
    setLoading(true);
    signIn(data).catch((err: any) => {
      setError('username', {
        message: 'Usuário ou senha estão incorretos!'
      });
      setLoading(false);
    });
  }, [signIn])

  return (
    <VStack justifyContent={'center'} bg={'gray.600'} w={'full'} h={'100vh'}>
      <Card shadow={'md'} minW={'330px'} py={8} bg={'whiteAlpha.900'} width={['80%', 'md']} h={['md']} alignItems={'center'}>
        <HStack>
          <motion.div 
            animate={{ transition: {
            duration: 2,
            type: 'keyframes',
            repeat: Infinity
          }, rotate: 360 }}
          >
            <Image src={ReactSVG} w={['50px', '75px', '80px']} h={['50px', '75px', '80px']}/>
          </motion.div>
          <Heading size={'lg'} fontFamily={'Montserrat'} fontWeight={500} color={'yellow.400'}>
            Simple Blog
          </Heading>
        </HStack>
        <VStack mt={'40px'} w={'80%'}>
          <Text mb={2}>Entre com o seu Usuario</Text>
          <InputGroup>
            <InputLeftAddon>
              <FontAwesomeIcon icon={faUser}/>
            </InputLeftAddon>
            <Input isInvalid={!!errors['username']} {...register('username')} placeholder='Insira o Usuario/Email'/>
          </InputGroup>
          <InputGroup>
            <InputLeftAddon>
              <FontAwesomeIcon icon={faLock}/>
            </InputLeftAddon>
            <Input isInvalid={!!errors['password']} {...register('password')} placeholder='Senha' type={'password'}/>
          </InputGroup>
          <Text>{(errors['username'] || errors['password'])?.message}</Text>

          <Button loadingText={'Entrando'} isLoading={loading} onClick={handleSubmit(onLogin)} w={'full'} colorScheme={'green'} type={'submit'}>
            Entrar
          </Button>

        </VStack>
        <Link style={{ marginTop: 12 }} to={'/register'}>
          Não tem uma conta? Cadastre-se
        </Link>
      </Card>
    </VStack>
  );
};

export default LoginScreen;
