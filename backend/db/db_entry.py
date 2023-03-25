#!/usr/bin/python3
from faker import Faker
import requests

# Set up Faker with the "en_US" locale
fake = Faker("en_US")

url = "http://localhost:8080"

# Set up Faker with the "en_US" locale
fake = Faker("en_US")

super_admin_credentials = [
    {"domain": "ucf.edu", "username": "superadmin@ucf.edu", "password": "password"},
    {"domain": "mit.edu", "username": "superadmin@mit.edu", "password": "password"},
    {"domain": "iu.edu", "username": "superadmin@iu.edu", "password": "password"},
]

# Define the university data for each university
universities = [
    {
        "name": "University of Central Florida",
        "location": "Orlando, FL",
        "description": "A public research university in Orlando, Florida.",
        "numStudents": 69000,
        "picture": "https://www.ucf.edu/wp-content/uploads/2021/06/UCF_Logo_PMS-1000x663.png",
        "emailDomain": "ucf.edu",
    },
    {
        "name": "Massachusetts Institute of Technology",
        "location": "Cambridge, MA",
        "description": "A private research university in Cambridge, Massachusetts.",
        "numStudents": 11000,
        "picture": "https://www.mit.edu/files/images/MIT_logo.png",
        "emailDomain": "mit.edu",
    },
    {
        "name": "Indiana University Bloomington",
        "location": "Bloomington, IN",
        "description": "A public research university in Bloomington, Indiana.",
        "numStudents": 49000,
        "picture": "https://www.indiana.edu/images/brand-resources/iub-primarylogos-06.png",
        "emailDomain": "iu.edu",
    },
]


# Login with the super admin credentials and create the universities
for creds, university_data in zip(super_admin_credentials, universities):
    response = requests.post(f"{url}/api/admin/register", json={
        "email": creds["username"],
        "firstName": "Super",
        "lastName": "Admin",
        "password": creds["password"],
    })
    if response.status_code == 201:
        print(f"Successfully created super admin: {creds['username']}")
    else:
        print(f"Failed to create super admin: {creds['username']}")
        
    # Login with the super admin credentials to get a JWT token
    response = requests.post(f"{url}/api/login", json={
        "username": creds["username"],
        "password": creds["password"],
    })
    if response.status_code != 200:
        print(f"Failed to login to {creds['domain']}")
        continue
    token = response.json()["token"]

    # Add the JWT token to the headers for authenticated requests
    headers = {"Authorization": f"Bearer {token}"}

    # Create the university using the authenticated request
    university_data["emailDomain"] = creds["domain"]  # Override the email domain with the super admin's domain
    response = requests.post(f"{url}/api/admin/createUniversity", json=university_data, headers=headers)
    if response.status_code == 201:
        print(f"Successfully created university: {university_data['name']}")
    else:
        print(f"Failed to create university: {university_data['name']}")



# Define the email domain for each university
ucf_domain = "ucf.edu"
mit_domain = "mit.edu"
iu_domain = "iu.edu"


# Fetch the list of universities from the API
response = requests.get(f"{url}/api/university/all")
universities = response.json()

ucf_university = next(
    (university for university in universities if university["emailDomain"] == ucf_domain),
    None,
)

mit_university = next(
    (university for university in universities if university["emailDomain"] == mit_domain),
    None,
)

iu_university = next(
    (university for university in universities if university["emailDomain"] == iu_domain),
    None,
)

# Initialize empty lists for storing created user data
ucf_users = []
mit_users = []
iu_users = []

# Create 10 users with @ucf.edu email
if ucf_university:
    for i in range(10):
        first_name = fake.first_name()
        last_name = fake.last_name()
        email = f"{first_name.lower()}.{last_name.lower()}@{ucf_domain}"
        password = "password"
        university_id = ucf_university["id"]
        data = {
            "user": {
                "email": email,
                "firstName": first_name,
                "lastName": last_name,
                "password": password,
            },
            "university": {"id": university_id},
        }
        response = requests.post(f"{url}/api/user/register", json=data)
        ucf_users.append(data["user"])  # Save the created UCF user data

# Create 5 users with @mit.edu email
if mit_university:
    for i in range(5):
        first_name = fake.first_name()
        last_name = fake.last_name()
        email = f"{first_name.lower()}.{last_name.lower()}@{mit_domain}"
        password = "password"
        university_id = mit_university["id"]
        data = {
            "user": {
                "email": email,
                "firstName": first_name,
                "lastName": last_name,
                "password": password,
            },
            "university": {"id": university_id},
        }
        response = requests.post(f"{url}/api/user/register", json=data)
        mit_users.append(data["user"])  # Save the created MIT user data


# Create 5 users with @iu.edu email
iu_university = next(
    (university for university in universities if university["emailDomain"] == iu_domain),
    None,
)
if iu_university:
    for i in range(5):
        first_name = fake.first_name()
        last_name = fake.last_name()
        email = f"{first_name.lower()}.{last_name.lower()}@{iu_domain}"
        password = "password"
        university_id = iu_university["id"]
        data = {
            "user": {
                "email": email,
                "firstName": first_name,
                "lastName": last_name,
                "password": password,
            },
            "university": {"id": university_id},
        }
        response = requests.post(f"{url}/api/user/register", json=data)
        print(response.json())


def login_and_get_token(email, password):
    response = requests.post(f"{url}/api/login", json={"username": email, "password": password})
    if response.status_code != 200:
        print(f"Failed to login to {email}")
        return None
    return response.json()["token"]


# Find a UCF student and an MIT student
ucf_student = ucf_users[0]
mit_student = mit_users[0]

if ucf_student and mit_student:
    # Login as UCF student and create an RSO
    token = login_and_get_token(ucf_student["email"], "password")
    if token:
        headers = {"Authorization": f"Bearer {token}"}
        rso_data = {"name": "New UCF RSO", "universityId": ucf_university["id"]}
        response = requests.post(f"{url}/api/rso/create", json=rso_data, headers=headers)
        rso = response.json()
        print(rso)

    # Login as MIT student and join the created RSO

    # Find 4 distinct UCF students and join the RSO
    ucf_students = [user for user in ucf_users if ucf_domain in user["email"] and user["email"] != ucf_student["email"]][:4]
    
    for student in ucf_students:
        token = login_and_get_token(student["email"], "password")
        if token:
            headers = {"Authorization": f"Bearer {token}"}
            response = requests.post(f"{url}/api/rso/join", json={"id": rso["id"]}, headers=headers)
            if response.status_code == 200:
                print(response.content)
