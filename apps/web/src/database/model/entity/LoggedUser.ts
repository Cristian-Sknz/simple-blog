import { Model, tableSchema, Relation } from '@nozbe/watermelondb';
import { immutableRelation } from '@nozbe/watermelondb/decorators';
import { Associations } from '@nozbe/watermelondb/Model';
import { Tables } from './Tables';
import BlogUser from './User';

class LoggedUser extends Model {
  public static table: string = Tables.LoggedUser;
  public static associations: Associations = {
    [Tables.BlogUser]: {
      type: 'belongs_to',
      key: 'user_id'
    }
  }

  @immutableRelation(Tables.BlogUser, 'user_id') user: Relation<BlogUser>;
}

const LoggedUserSchema = tableSchema({
  name: Tables.LoggedUser,
  columns: [
    { name: 'user_id', type: 'string', isIndexed: true }
  ]
});

export { LoggedUserSchema };
export default LoggedUser;