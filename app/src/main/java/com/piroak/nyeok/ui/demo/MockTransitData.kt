package com.piroak.nyeok.ui.demo

import com.google.maps.routing.v2.Route
import com.google.protobuf.util.JsonFormat

fun getMockRoute(): Route {
    val builder: Route.Builder = Route.newBuilder()
    JsonFormat.parser().merge(mockData, builder)
    return builder.build()
}

const val mockData = """
    {
  "legs": [{
    "distanceMeters": 5610,
    "duration": "1301s",
    "staticDuration": "1301s",
    "polyline": {
      "encodedPolyline": "g_zzEusitWmJkEyYmAs@BKLCVFdBAf@SnBy@vBCj@Fl@VbADz@eBxIsEtT}@vEsCdN[j@a@^a@Rc@BkI]QGUUYs@_@qAQaAYk@YcAAQpFoWpA_GDm@vCmN@[C]U_ASqABMlLoEt@Wr@XRZ^HjCs@XETW@_@S{@u@oAaA{@y@]iAOy@@s@JIG}@eEMc@U{AEcAB_ANuAv@yD~HaSrC}JR{@Fu@@eJRc@fAc@rGwB`BEpFRj@fJ"
    },
    "startLocation": {
      "latLng": {
        "latitude": 36.019241799999996,
        "longitude": 129.3242741
      }
    },
    "endLocation": {
      "latLng": {
        "latitude": 36.0214277,
        "longitude": 129.3370694
      }
    },
    "steps": [{
      "distanceMeters": 222,
      "staticDuration": "223s",
      "polyline": {
        "encodedPolyline": "g_zzEusitWmJkE"
      },
      "startLocation": {
        "latLng": {
          "latitude": 36.019241799999996,
          "longitude": 129.3242741
        }
      },
      "endLocation": {
        "latLng": {
          "latitude": 36.021066,
          "longitude": 129.325293
        }
      },
      "navigationInstruction": {
      },
      "localizedValues": {
        "distance": {
          "text": "0.2 km"
        },
        "staticDuration": {
          "text": "4분"
        }
      },
      "travelMode": "WALK"
    }, {
      "distanceMeters": 5224,
      "staticDuration": "913s",
      "polyline": {
        "encodedPolyline": "ujzzEazitWyYmAs@BKLCVFdBAf@SnBy@vBCj@Fl@VbADz@eBxIsEtT}@vEsCdN[j@a@^a@Rc@BkI]QGUUYs@_@qAQaAYk@YcAAQpFoWpA_GDm@vCmN@[C]U_ASqABMlLoEt@Wr@XRZ^HjCs@XETW@_@S{@u@oAaA{@y@]iAOy@@s@JIG}@eEMc@U{AEcAB_ANuAv@yD~HaSrC}JR{@Fu@@eJRc@fAc@rGwB`BEpFR"
      },
      "startLocation": {
        "latLng": {
          "latitude": 36.021066,
          "longitude": 129.325293
        }
      },
      "endLocation": {
        "latLng": {
          "latitude": 36.021651999999996,
          "longitude": 129.338873
        }
      },
      "navigationInstruction": {
        "instructions": "버스 문덕.유강행행"
      },
      "localizedValues": {
        "distance": {
          "text": "5.2 km"
        },
        "staticDuration": {
          "text": "15분"
        }
      },
      "transitDetails": {
        "stopDetails": {
          "arrivalStop": {
            "name": "대잠센트럴하이츠",
            "location": {
              "latLng": {
                "latitude": 36.021651999999996,
                "longitude": 129.338873
              }
            }
          },
          "arrivalTime": "2024-08-12T14:35:30Z",
          "departureStop": {
            "name": "포항제철공고",
            "location": {
              "latLng": {
                "latitude": 36.021066,
                "longitude": 129.325293
              }
            }
          },
          "departureTime": "2024-08-12T14:20:17Z"
        },
        "localizedValues": {
          "arrivalTime": {
            "time": {
              "text": "오후 11:35"
            },
            "timeZone": "Asia/Seoul"
          },
          "departureTime": {
            "time": {
              "text": "오후 11:20"
            },
            "timeZone": "Asia/Seoul"
          }
        },
        "headsign": "문덕.유강행",
        "headway": "1320s",
        "transitLine": {
          "agencies": [{
            "name": "경상북도버스운송사업조합",
            "uri": "http://www.odsay.com/"
          }],
          "name": "경북 간선버스",
          "color": "#374ff2",
          "nameShort": "306(기본)(문덕.유강행)",
          "textColor": "#ffffff",
          "vehicle": {
            "name": {
              "text": "버스"
            },
            "type": "BUS",
            "iconUri": "//maps.gstatic.com/mapfiles/transit/iw2/6/bus2.png"
          }
        },
        "stopCount": 12
      },
      "travelMode": "TRANSIT"
    }, {
      "distanceMeters": 164,
      "staticDuration": "165s",
      "polyline": {
        "encodedPolyline": "inzzE}nltWj@fJ"
      },
      "startLocation": {
        "latLng": {
          "latitude": 36.021651999999996,
          "longitude": 129.338873
        }
      },
      "endLocation": {
        "latLng": {
          "latitude": 36.0214277,
          "longitude": 129.3370694
        }
      },
      "navigationInstruction": {
      },
      "localizedValues": {
        "distance": {
          "text": "0.2 km"
        },
        "staticDuration": {
          "text": "3분"
        }
      },
      "travelMode": "WALK"
    }],
    "localizedValues": {
      "distance": {
        "text": "5.6 km"
      },
      "duration": {
        "text": "22분"
      },
      "staticDuration": {
        "text": "22분"
      }
    },
    "stepsOverview": {
      "multiModalSegments": [{
        "stepStartIndex": 0,
        "stepEndIndex": 0,
        "navigationInstruction": {
          "instructions": "포항제철공고까지 도보"
        },
        "travelMode": "WALK"
      }, {
        "stepStartIndex": 1,
        "stepEndIndex": 1,
        "navigationInstruction": {
          "instructions": "버스 문덕.유강행행"
        },
        "travelMode": "TRANSIT"
      }, {
        "stepStartIndex": 2,
        "stepEndIndex": 2,
        "travelMode": "WALK"
      }]
    }
  }],
  "distanceMeters": 5610,
  "duration": "2621s",
  "staticDuration": "2621s",
  "polyline": {
    "encodedPolyline": "g_zzEusitWmJkEyYmAs@BKLCVFdBAf@SnBy@vBCj@Fl@VbADz@eBxIsEtT}@vEsCdN[j@a@^a@Rc@BkI]QGUUYs@_@qAQaAYk@YcAAQpFoWpA_GDm@vCmN@[C]U_ASqABMlLoEt@Wr@XRZ^HjCs@XETW@_@S{@u@oAaA{@y@]iAOy@@s@JIG}@eEMc@U{AEcAB_ANuAv@yD~HaSrC}JR{@Fu@@eJRc@fAc@rGwB`BEpFRj@fJ"
  },
  "viewport": {
    "low": {
      "latitude": 36.019241799999996,
      "longitude": 129.31330409999998
    },
    "high": {
      "latitude": 36.031675,
      "longitude": 129.338971
    }
  },
  "travelAdvisory": {
    "transitFare": {
      "currencyCode": "KRW",
      "units": "1200"
    }
  },
  "localizedValues": {
    "distance": {
      "text": "5.6 km"
    },
    "duration": {
      "text": "44분"
    },
    "staticDuration": {
      "text": "44분"
    },
    "transitFare": {
      "text": "₩1,200"
    }
  },
  "routeLabels": ["DEFAULT_ROUTE"]
}
"""