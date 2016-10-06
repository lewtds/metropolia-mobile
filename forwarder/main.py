# fetch lunch menu from ihana and store into Firebase

import requests

# FIXME: this library is kinda old and unmaintained,
# consider forking it or work with plain old HTTP
from firebase import firebase


# FIXME: Figure out how to get this info dynamically, seems
# hardcoded in ihana's source code
CAMPUSES = ['bulevardi', 'vanhamaantie']
FOOD_PROVIDERS = ['sodexo']


def lunch_menu_url(campus='vanhamaantie'):
    template = "https://ihana.metropolia.fi/proxy.php?url=http://mob.metropolia.fi/ruoka/ruoka.php?toimipiste={}%ravintola={}%whole_week=1%json=1%"

    return template.format(campus, 'sodexo')

def process_lunch_menu(menu):
    # the data returned by ihana is of the form
    # ["Finnish text / English text", "Finnish text / English text"]
    # we want to convert it to the form
    # [{descriptionFi: "Finnish text", descriptionEn: "English text"}]
    # other fields (like lunch type, price, etc. might be added later)

    def process_item(item):
        fin, eng = item.split(" / ")
        return {"descriptionFi": fin, "descriptionEn": eng}

    return map(process_item, menu)

def process_week_menu(week_menu):
    # week_menu is a object with string keys representing the date (1 for monday)
    return map(process_lunch_menu, week_menu.values())

def debug(value):
    print value
    return value

res = requests.get(debug(lunch_menu_url('vanhamaantie')))
print res.json()

fb = firebase.FirebaseApplication('https://metropolia-1dad9.firebaseio.com/', None)

fb.put("/food/lunch_menu/vanhamaantie/", 'current_week', process_week_menu(res.json()))

