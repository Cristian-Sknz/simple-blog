import { Model, Relation } from '@nozbe/watermelondb';
import { immutableRelation, date } from '@nozbe/watermelondb/decorators';
import { Associations } from '@nozbe/watermelondb/Model';
import { tableSchema } from '@nozbe/watermelondb/Schema';
import { Tables } from './Tables';
import Post from './Post';
import BlogUser from './User';

class PostLike extends Model {
  static table: string = Tables.PostLike;
  static associations: Associations = {
    [Tables.Post]: {
      key: 'post_id',
      type: 'belongs_to'
    },
    [Tables.BlogUser]: {
      key: 'user_id',
      type: 'belongs_to'
    }
  }

  @immutableRelation(Tables.Post, 'post_id') post: Relation<Post>;
  @immutableRelation(Tables.BlogUser, 'user_id') user: Relation<BlogUser>;

  @date('created_at') createdAt: Date;
  @date('updated_at') updatedAt: Date;
}

const PostLikeSchema = tableSchema({
  name: Tables.PostLike,
  columns: [
    { name: 'post_id', type: 'string', isIndexed: true },
    { name: 'user_id', type: 'string', isIndexed: true },
    { name: 'created_at', type: 'number' },
    { name: 'updated_at', type: 'number' }
  ]
});

export { PostLikeSchema };
export default PostLike;