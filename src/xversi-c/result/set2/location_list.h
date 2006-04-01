#ifndef LOCATION_LIST_H
#define LOCATION_LIST_H

#include <list>


class location_list
{
public:
  typedef struct { int x ; int y ; } location ;
  typedef std::list<location>::iterator iterator ;

  inline int get_num_loc(void) { return loc_list.size() ; }
  inline bool is_empty(void) { return loc_list.empty() ; }
  inline void insert(location& loc) { loc_list.push_front(loc) ; }
  inline void clear(void) { loc_list.clear() ; }
  inline iterator begin(void) { return loc_list.begin() ; }
  inline iterator end(void) { return loc_list.end() ; }

protected:
  std::list<location> loc_list ;
} ;

#endif // LOCATION_LIST_H
