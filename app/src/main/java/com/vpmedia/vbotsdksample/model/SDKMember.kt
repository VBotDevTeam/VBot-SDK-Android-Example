package com.vpmedia.vbotsdksample.model

data class SDKMember(
    val type: String,
    val typeTest: String,
    val name: String,
    val userId: String,
    val code: String,
) {
    companion object {
        val testAccount = listOf(
            // XanhSM
            SDKMember(
                type = "ios",
                typeTest = "xanhsm",
                name = "XanhSM iOS 01",
                userId = "bfd6aac4-40fb-4866-98b7-e3c71dc3a948",
                code = "ios11",
            ),
            SDKMember(
                type = "ios",
                typeTest = "xanhsm",
                name = "XanhSM iOS 02",
                userId = "26b1fdef-dd60-4b75-85bd-07ee600a4fdd",
                code = "ios12",
            ),
            SDKMember(
                type = "ios",
                typeTest = "xanhsm",
                name = "XanhSM iOS 03",
                userId = "79c6825e-b24e-4d9a-9f99-3cb78f58f13b",
                code = "ios13",
            ),
            SDKMember(
                type = "ios",
                typeTest = "xanhsm",
                name = "XanhSM iOS 04",
                userId = "aaa056f9-7c7f-4f92-91a1-6d285e64d394",
                code = "ios14",
            ),
            SDKMember(
                type = "ios",
                typeTest = "xanhsm",
                name = "XanhSM iOS 05",
                userId = "2f095a09-d43a-4719-85f4-2bf0e233c831",
                code = "ios15",
            ),
            SDKMember(
                type = "android",
                typeTest = "xanhsm",
                name = "XanhSM Android 01",
                userId = "b54be6d4-4e77-4147-918a-e2c714152a9e",
                code = "android11",
            ),
            SDKMember(
                type = "android",
                typeTest = "xanhsm",
                name = "XanhSM Android 02",
                userId = "91affd4c-2826-441a-a01b-e0c1b49d7c7a",
                code = "android12",
            ),
            SDKMember(
                type = "android",
                typeTest = "xanhsm",
                name = "XanhSM Android 03",
                userId = "a00ed97a-f1c5-4f20-93a9-e2dc17891773",
                code = "android13",
            ),
            SDKMember(
                type = "android",
                typeTest = "xanhsm",
                name = "XanhSM Android 04",
                userId = "7e3d4cd1-be29-46f6-af23-3ac70b7bd664",
                code = "android14",
            ),
            SDKMember(
                type = "android",
                typeTest = "xanhsm",
                name = "XanhSM Android 05",
                userId = "c7e58e8a-ffc4-4df2-a31c-e222a355a9ce",
                code = "android15",
            ),
            // VBot
            SDKMember(
                type = "ios",
                typeTest = "vbot",
                name = "VBot iOS 01",
                userId = "31f8ea9e-7122-42a7-bde2-33b3d01c2e0e",
                code = "ios01",
            ),
            SDKMember(
                type = "ios",
                typeTest = "vbot",
                name = "VBot iOS 02",
                userId = "44849964-1abf-4e3a-85a4-169dcb4a7e11",
                code = "ios02",
            ),
            SDKMember(
                type = "ios",
                typeTest = "vbot",
                name = "VBot iOS 03",
                userId = "171d6992-b15f-4848-9fb3-e7eec710adfa",
                code = "ios03",
            ),
            SDKMember(
                type = "ios",
                typeTest = "vbot",
                name = "VBot iOS 04",
                userId = "451a514f-4ea5-4ead-8465-fba2314bdad6",
                code = "ios04",
            ),
            SDKMember(
                type = "ios",
                typeTest = "vbot",
                name = "VBot iOS 05",
                userId = "e9648847-9396-4892-b886-c38194fb4c14",
                code = "ios05",
            ),
            SDKMember(
                type = "ios",
                typeTest = "vbot",
                name = "VBot iOS 06",
                userId = "a0cec24e-38ae-4185-826e-3e0d9994f87e",
                code = "ios06",
            ),
            SDKMember(
                type = "ios",
                typeTest = "vbot",
                name = "VBot iOS 07",
                userId = "e8811925-9856-4c08-a686-d8fcf4d1d00e",
                code = "ios07",
            ),
            SDKMember(
                type = "ios",
                typeTest = "vbot",
                name = "VBot iOS 08",
                userId = "da4bbb4c-38c7-4992-822a-589772d45493",
                code = "ios08",
            ),
            SDKMember(
                type = "ios",
                typeTest = "vbot",
                name = "VBot iOS 09",
                userId = "b28701f8-1977-4cb1-9ba0-0af3d7d04429",
                code = "ios09",
            ),
            SDKMember(
                type = "ios",
                typeTest = "vbot",
                name = "VBot iOS 10",
                userId = "5afcecd3-38e5-4e59-882e-5241c79ba74a",
                code = "ios10",
            ),
            SDKMember(
                type = "android",
                typeTest = "vbot",
                name = "VBot Android 01",
                userId = "bf51cd35-865d-4306-b5c6-89717aa3438f",
                code = "android01",
            ),
            SDKMember(
                type = "android",
                typeTest = "vbot",
                name = "VBot Android 02",
                userId = "1f85475f-4b7e-4ed2-a2f6-0161c92a85d6",
                code = "android02",
            ),
            SDKMember(
                type = "android",
                typeTest = "vbot",
                name = "VBot Android 03",
                userId = "1f021e88-5f27-42fe-8137-be60272e73b9",
                code = "android03",
            ),
            SDKMember(
                type = "android",
                typeTest = "vbot",
                name = "VBot Android 04",
                userId = "30096c22-13dc-4c66-b5ed-8c1eb759da39",
                code = "android04",
            ),
            SDKMember(
                type = "android",
                typeTest = "vbot",
                name = "VBot Android 05",
                userId = "36e0fa9c-8098-4e18-8942-68fac944cfa7",
                code = "android05",
            ),
            SDKMember(
                type = "android",
                typeTest = "vbot",
                name = "VBot Android 06",
                userId = "15748dc5-fa14-44fd-84ad-3a1e07719f70",
                code = "android06",
            ),
            SDKMember(
                type = "android",
                typeTest = "vbot",
                name = "VBot Android 07",
                userId = "186bc635-ea47-410b-99d9-cea887d95656",
                code = "android07",
            ),
            SDKMember(
                type = "android",
                typeTest = "vbot",
                name = "VBot Android 08",
                userId = "b4d13bb6-6ebd-42a3-8040-807e9a41a0df",
                code = "android08",
            ),
            SDKMember(
                type = "android",
                typeTest = "vbot",
                name = "VBot Android 09",
                userId = "4c89c73e-8eef-4aac-9f62-4d99830f796f",
                code = "android09",
            ),
            SDKMember(
                type = "android",
                typeTest = "vbot",
                name = "VBot Android 10",
                userId = "ad647262-8a62-42ed-aa44-893d179e83d7",
                code = "android10",
            ),
        )


        fun getByName(name: String): SDKMember? {
            return testAccount.find { it.name == name }
        }

        fun getLoginXanhSM(): List<SDKMember> {
            return testAccount.filter { it.type == "android" && it.typeTest == "xanhsm" }
        }

        fun getLoginVBot(): List<SDKMember> {
            return testAccount.filter { it.type == "android" && it.typeTest == "vbot" }
        }

        fun getCall(): List<SDKMember>{
            return testAccount
        }
    }
}