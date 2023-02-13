const functions = require("firebase-functions");
const admin = require("firebase-admin")

admin.initializeApp();

const db = admin.firestore();
const testCollection = "ListData";
const refCollection = "Refrigerator";

exports.everyDayTask = functions
    .region("asia-northeast3") // 가장 가까운 리전 선택 (한국 기준 northeast3)
    .pubsub.schedule("every day 23:30") // 매일 오 11시 30분에 실행할 함수
    .timeZone("Asia/Seoul") // 타임존 선택
    .onRun(async (_) => {

        const RefDocs = await db
            .collectionGroup(refCollection)
            .get()
        if(!RefDocs.empty) {
            for (const refDoc of RefDocs.docs) {
                for (key in refDoc.data()) {
                    if (key == "expirydate") {
                        refDoc.ref.update({"expirydate": refDoc.data()[key]-1})
                    }
                }
            }
        }
    })
