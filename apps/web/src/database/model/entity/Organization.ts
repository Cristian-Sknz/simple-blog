class Organization {
  id: string
  name: string
  ownerId: string
  members: string[]
  invites: string[]
  createdAt: string
  updatedAt: string
  public: true
  table: "organizations"
}

export default Organization;