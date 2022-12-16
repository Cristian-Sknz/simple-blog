import { Model, tableSchema, Relation, Query, Q } from '@nozbe/watermelondb';
import { text, date, children, readonly, immutableRelation, writer } from '@nozbe/watermelondb/decorators'
import { Associations } from '@nozbe/watermelondb/Model';
import { Tables } from './Tables';
import Comment from './Comment';
import PostLike from './PostLike';
import BlogUser from './User';

class Post extends Model {
  static table: string = Tables.Post;
  static associations: Associations = {
    [Tables.BlogUser]: {
      type: 'belongs_to',
      key: 'user_id'
    },
    [Tables.Comment]: {
      type: 'has_many',
      foreignKey: 'post_id'
    },
    [Tables.PostLike]: {
      type: 'has_many',
      foreignKey: 'post_id'
    }
  }

  @text('title') title: string;
  @text('subtitle') subtitle?: string;
  @text('content') content: string;

  @readonly @date('created_at') createdAt: Date;
  @readonly @date('updated_at') updatedAt: Date;

  @immutableRelation(Tables.BlogUser, 'user_id') user: Relation<BlogUser>;
  @children(Tables.Comment) comments: Query<Comment>;
  @children(Tables.PostLike) likes: Query<PostLike>;

  @writer async addComment(content: string) {
    const logged = localStorage.getItem('logged_user')
    if (!logged) {
      throw 'Usuário logado não foi encontrado'
    }

    const user = await this.collections.get<BlogUser>(BlogUser.table).find(logged);
    await this.comments.collection.create(comment => {
      comment.content = content;
      comment.user.set(user);
      comment.post.set(this);
    });
  }

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
      like.post.set(this);
      like.user.set(user);
    });
  }
}

const PostSchema = tableSchema({
  name: 'posts',
  columns: [
    { name: 'title', type: 'string'},
    { name: 'subtitle', type: 'string', isOptional: true },
    { name: 'content', type: 'string' },
    { name: 'user_id', type: 'string', isIndexed: true },
    { name: 'created_at', type: 'number' },
    { name: 'updated_at', type: 'number' }
  ]
});

export { PostSchema };
export default Post;