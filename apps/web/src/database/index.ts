import { appSchema, Database } from '@nozbe/watermelondb';
import LokiJSAdapter from '@nozbe/watermelondb/adapters/lokijs';
import { setGenerator } from '@nozbe/watermelondb/utils/common/randomId'
import {
  PostSchema,
  CommentSchema,
  BlogUserSchema,
  PostLikeSchema,
  CommentLikeSchema,
} from './model/entity';

import {
  Post,
  Comment,
  BlogUser,
  PostLike,
  CommentLike,
} from './model/entity';

import { v4 } from 'uuid'

setGenerator(() => v4())

const adapter = new LokiJSAdapter({
  dbName: 'simple-blog',
  schema: appSchema({
    tables: [
      PostSchema,
      CommentSchema,
      BlogUserSchema,
      PostLikeSchema,
      CommentLikeSchema,
    ],
    version: 1,
  }),
  useIncrementalIndexedDB: true,
  useWebWorker: false,
});

export const database = new Database({
  adapter,
  modelClasses: [Post, Comment, BlogUser, PostLike, CommentLike],
});

export * from './model/entity';
