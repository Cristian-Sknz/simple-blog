import { Model, Relation } from '@nozbe/watermelondb';
import { date, immutableRelation } from '@nozbe/watermelondb/decorators';
import { Associations } from '@nozbe/watermelondb/Model';
import { tableSchema } from '@nozbe/watermelondb/Schema';
import { Tables } from './Tables';
import BlogUser from './User';
import Comment from './Comment';

class CommentLike extends Model {
  static table: string = Tables.CommentLike;
  static associations: Associations = {
    [Tables.Comment]: {
      key: 'comment_id',
      type: 'belongs_to'
    },
    [Tables.BlogUser]: {
      key: 'user_id',
      type: 'belongs_to'
    }
  }

  @immutableRelation(Tables.Comment, 'comment_id') comment: Relation<Comment>;
  @immutableRelation(Tables.BlogUser, 'user_id') user: Relation<BlogUser>;

  @date('created_at') createdAt: Date;
  @date('updated_at') updatedAt: Date;
}

const CommentLikeSchema = tableSchema({
  name: Tables.CommentLike,
  columns: [
    { name: 'comment_id', type: 'string', isIndexed: true },
    { name: 'user_id', type: 'string', isIndexed: true },
    { name: 'created_at', type: 'number' },
    { name: 'updated_at', type: 'number' }
  ]
});

export { CommentLikeSchema };
export default CommentLike;