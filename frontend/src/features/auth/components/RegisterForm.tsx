import { useForm } from 'react-hook-form';
import * as yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';
import { registerUser } from '../api';

type RegisterValues = {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
};

const validationSchema = yup.object({
  firstName: yup.string().required('First name is required'),
  lastName: yup.string().required('Last name is required'),
  email: yup.string().required('Email is required').email('Email must be valid'),
  password: yup
    .string()
    .required('Password is required')
    .min(8, 'Password should contain at least eight characters'),
});

export const RegisterForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterValues>({ mode: 'onBlur', resolver: yupResolver(validationSchema) });
  const onSubmit = handleSubmit(async (data) => {
    const response = await registerUser(data);
    console.log(response);
  });

  console.log(import.meta.env.VITE_BACKEND_BASE_URL);

  return (
    <>
      <h1>Register Form</h1>
      <form onSubmit={onSubmit}>
        <input {...register('firstName')} placeholder="First name" />
        {errors?.firstName && <p>{errors.firstName.message}</p>}
        <input {...register('lastName')} placeholder="Last name" />
        {errors?.lastName && <p>{errors.lastName.message}</p>}
        <input type="email" {...register('email')} placeholder="Email" />
        {errors?.email && <p>{errors.email.message}</p>}
        <input type="password" {...register('password')} placeholder="Password" />
        {errors?.password && <p>{errors.password.message}</p>}

        <button onClick={onSubmit}>Register</button>
      </form>
    </>
  );
};
