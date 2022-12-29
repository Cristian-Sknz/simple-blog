import { Model, tableSchema, Collection } from '@nozbe/watermelondb';
import { children, text, date } from '@nozbe/watermelondb/decorators';
import { Associations } from '@nozbe/watermelondb/Model';
import Post from './Post';
import { Tables } from './Tables';

class BlogUser extends Model {
  static table: string = Tables.BlogUser;
  static associations: Associations = {
    [Tables.Post]: {
      type: 'has_many',
      foreignKey: 'user_id'
    }
  }

  @text('name') name: string;
  @text('username') username: string;
  @text('email') email: string;
  @text('image') image: string;

  @date('created_at') createdAt: Date;
  @date('updated_at') updatedAt: Date;

  @children(Tables.Post) posts: Collection<Post>;
}

const BlogUserSchema = tableSchema({
  name: Tables.BlogUser,
  columns: [
    { name: 'name', type: 'string' },
    { name: 'username', type: 'string' },
    { name: 'email', type: 'string' },
    { name: 'image', type: 'string', isOptional: true },
    { name: 'created_at', type: 'number' },
    { name: 'updated_at', type: 'number' }
  ]
})

export { BlogUserSchema }
export default BlogUser;