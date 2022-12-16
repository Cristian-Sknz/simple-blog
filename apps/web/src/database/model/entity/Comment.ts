import { Model, Relation, Query, Q } from '@nozbe/watermelondb';
import { text, readonly, date, immutableRelation, children, writer } from '@nozbe/watermelondb/decorators';
import { Associations } from '@nozbe/watermelondb/Model';
import { tableSchema } from '@nozbe/watermelondb/Schema';
import { Tables } from './Tables';
import Post from './Post';
import BlogUser from './User';
import CommentLike from './CommentLike';

class Comment extends Model {
  static table: string = Tables.Comment;
  static associations: Associations = {
    [Tables.BlogUser]: {
      key: 'user_id',
      type: 'belongs_to'
    },
    [Tables.Post]: {
      key: 'post_id',
      type: 'belongs_to'
    },
    [Tables.CommentLike]: {
      type: 'has_many',
      foreignKey: 'comment_id',
    }
  }

  @text('content') content: string;

  @immutableRelation(Tables.Post, 'post_id') post:  Relation<Post>;
  @immutableRelation(Tables.BlogUser, 'user_id') user:  Relation<BlogUser>;
  @children(Tables.CommentLike) likes: Query<CommentLike>;

  @readonly @date('created_at') createdAt: Date;
  @readonly @date('updated_at') updatedAt: Date;


  @writer async pushLike() {
    const logged = localStorage.getItem('logged_user')
    if (!logged) {
      throw 'Usuário logado não foi encontrado'
    }
    
    const user = await this.collections.get<BlogUser>(BlogUser.table).find(logged);
    const userLike = await this.likes.extend(Q.where('user_id', user.id)).fetch();
    if (userLike[0]) {
      await userLike[0].markAsDeleted();
      return;
    }
    await this.likes.collection.create(like => {
      like.comment.set(this);
      like.user.set(user);
    });
  }
}

const CommentSchema = tableSchema({
  name: 'comments',
  columns: [
    { name: 'content', type: 'string' },
    { name: 'user_id', type: 'string', isIndexed: true },
    { name: 'post_id', type: 'string', isIndexed: true },
    { name: 'created_at', type: 'number' },
    { name: 'updated_at', type: 'number' }
  ]
});

export { CommentSchema };
export default Comment;