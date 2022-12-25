import Post, { PostSchema } from './Post';
import Comment, { CommentSchema } from './Comment';
import BlogUser, { BlogUserSchema } from './User';
import PostLike, { PostLikeSchema } from './PostLike';
import CommentLike, { CommentLikeSchema } from './CommentLike';
import Organization from './Organization';
import Model from '@nozbe/watermelondb/Model';

type SyncModelObject<T, K extends string = 'created' | 'updated'> = {
  [key in K]: T[];
} & { deleted: string[] }

export type SyncChanges = {
  'post_likes': SyncModelObject<PostLike>
  'posts': SyncModelObject<Post>
  'users': SyncModelObject<BlogUser>
  'comments': SyncModelObject<Comment>
  'comment_likes': SyncModelObject<CommentLike>
} & { [key: string]: SyncModelObject<Model> }

export { Post, Comment, BlogUser, PostLike, CommentLike, Organization };
export { PostSchema, CommentSchema, BlogUserSchema, PostLikeSchema, CommentLikeSchema };